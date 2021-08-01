<?php
session_start();
	
		if($_GET['lang'] != ""){
			$_SESSION['languge'] = $_GET['lang'];
			include('lang/'.$_SESSION['languge'].".php");
		}elseif($_SESSION['languge'] == ''){
			$_SESSION['languge'] = "tn";
			include("lang/tn.php");
		}else{
			include('lang/'.$_SESSION['languge'].".php");
		}


?>

<!DOCTYPE html>


<meta charset="UTF-8" />


<head>
<!-- Styles -->
<link rel="icon" type="image/png" href="images/logo.png">
<link type="text/css" rel="stylesheet" href="assets/plugins/materialize/css/materialize.min.css"/>
     
 <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
     
 <link href="assets/plugins/material-preloader/css/materialPreloader.min.css" rel="stylesheet">        
<link rel="stylesheet" href="css/setting-panel.css">
        	
        <!-- Theme Styles -->
 <link href="assets/css/alpha.min.css" rel="stylesheet" type="text/css"/>
 <link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>

   <!----script---->
<script src="assets/plugins/jquery/jquery-2.2.0.min.js"></script>
</head>



    <!--[if IE]><script lang="javascript" type="text/javascript" src="../../js/excanvas.js"></script><![endif]-->  				
<style>
	tr, td {
		border: solid 1px #CCC

	}
	.mycharts{

		margin: auto;
    width: 70%;
    border: 0px solid green;
    padding: 10px;
	}
	.chart{
		
		
		
		margin-right: 200px;
		
	    
		
		
	}
	.chart h1{
		font-size:16px;
		font-weight: bold;
		font-family: monospace;
	}
	
	.fft{
		width:100%;
		margin-top:40%;
		margin-bottom:50%;
		margin-left:100px;
		margin-right: 50px;
		padding: 1px;
		float: right;
	}
	.fft h1{
		font-size:16px;
		font-weight: bold;
		font-family: monospace;
	}
	.canvasjs-chart-credit {
   display: none;
}
 a:link:after, a:visited:after {    
  content: " ";    
  font-size: 90%;   
}
</style>

<?php
$path = "/home/pi/Desktop/vAlert/rec/".$_GET['file'];
$serial_number=substr($_GET['file'],25,4);
$datetime=substr($_GET['file'],9,4)."-".substr($_GET['file'],13,2)."-".substr($_GET['file'],15,2)." ".substr($_GET['file'],18,2).":".substr($_GET['file'],20,2).":".substr($_GET['file'],22,2);
$data_x=array();
$data_y=array();
$data_z=array();
$fft_x=array();
$fft_y=array();
$fft_z=array();
$hold_array=array();
$sample_rate_array=array();
$sample_rate=0;
error_reporting(E_ALL);
ini_set('display_errors', 1);
$line=1;
	if (($handle = fopen($path, "r")) !== FALSE) {
		$i = 0;
		 $row=1;
		  
		while (($data = fgetcsv($handle, 10000, ",")) !== FALSE) {
			   $num = $row % 60;
               if($line>1&& $line<=7)
               {
                  $fft_z[]=substr($data[0],31,-1);
                  $fft_x[]=$data[1];
                  $fft_y[]=$data[2];
               }
               else if($line>8)
               {
                   
			    
			     $sample_rate_array[]=$data[0]; 
				$data_x[] = $data[2];
				$data_y[] = $data[3];
				$data_z[] = $data[1];
				
				$i++;
				
			       
               }
			    
			
			$line++;
			
		}
		fclose($handle);
	}



$radial=array();
foreach($data_x as $key=>$value)
{
$radial[]="{x:".$key.",y:".$value."}";
} 
$vertical=array();
foreach($data_z as $key=>$value)
{
$vertical[]="{x:".$key.",y:".$value."}";
}
$transverse=array();

foreach($data_y as $key=>$value)
{
$transverse[]="{x:".$key.",y:".$value."}";
}
$sample_rate=1/$sample_rate_array[1];
$total_sample=$line-6;
$fft_vertical="{x:".$fft_z[4].",y:".$fft_z[3]."}";
$fft_radial="{x:".$fft_x[4].",y:".$fft_x[3].",markerType:\"triangle\"}";
$fft_transverse="{x:".$fft_y[4].",y:".$fft_y[3].",markerType:\"square\"}";	
$logpath="/home/pi/Desktop/vAlert/log/info.txt";

$fp=fopen($logpath,"r");
$mygps=array();
while(($buf=stream_get_line($fp,40,"\n"))!==false)
{
    $mygps[]=$buf;
}

$location=(isset($mygps[1]))?$mygps[1]:'';
$displacement_z="{x:".$fft_z[1]." ,y:".$fft_z[2]."}";
$displacementt_x="{x:".$fft_x[1]." ,y:".$fft_x[2].",markerType:\"triangle\"}";
$displacement_y="{x:".$fft_y[1]." ,y:".$fft_y[2].",markerType:\"square\"}";

$displacement_max=max($fft_z[2],$fft_x[2],$fft_y[2]);

$tableContent=explode(",",$_GET['table'])

?>
<body>
  
  
		
       
      <div class="mycharts main-form" style=" text-align:center;"> 

      	<h3>DIN EVENT Report <?php echo $datetime; ?>  </h3>
       <fieldset style="float:left;">
	<div>
		<font style="font-weight: bold;">Serial Number</font><br>
		<b><?php echo  $serial_number;?></b><br>
		<font style="font-weight: bold;">Total samples</font><br>
		<b><?php echo $total_sample ;?></b>
		<?php for($i=0;$i<count($mygps)-3;$i++){?>
		<br>
		<?php }?>

	</div>          
</fieldset>
<fieldset>
	<div>
		<font style="font-weight: bold;">Sampling Rate</font><br>
		<b><?php echo $sample_rate;?></b><br>
		<font style="font-weight: bold;">Number of channels</font><br>
		<b>3</b><br>
		<?php for($i=0;$i<count($mygps)-4;$i++){?>
		<br>
		<?php }?>
	</div>          
</fieldset>
<fieldset style="float:right;">
	<div>
		<?php
            $size=max(count($mygps),4);
		 for($i=0;$i<count($mygps);$i++){?>
		<font style="font-weight: bold;"><?= $mygps[$i]?></font><br>
		<?php } for($i=0;$i<$size-count($mygps);$i++){?>
		    <br>
		    <?php }?>
	</div>          
</fieldset>
      	<br><br><br>

      <table border="1" width="100%" style="text-align: center;border:3px #CCC solid;">
					<tr>
						<th>Channel </th>
						<th>Vertical</th>
						<th>Radial</th>
						<th>Transverse </th>
						
					</tr>
					<?php if (in_array('Acceleration(gal)',$tableContent)) {
						
					 ?>
					<tr>
						<th>Acceleration(gal)</th>
						<td><?php echo $fft_z[0];?></td>
						<td><?php echo $fft_x[0];?></td>
						<td><?php echo $fft_y[0];?> </td>
						
					</tr>
				   <?php } if (in_array('Velocity(mm/s)',$tableContent)) {
				   	
				   ?>
					<tr>
						<th>Velocity (mm/s) </th>
						<td><?php echo $fft_z[1];?></td>
						<td><?php echo $fft_x[1];?> </td>
						<td><?php echo $fft_y[1];?></td>
						
					</tr>
					<?php } if (in_array('Displacement(mm)',$tableContent)) {
						
					 ?>
					<tr>
						<th>Displacement (mm)</th>
						<td><?php echo $fft_z[2]; ?></td>
						<td><?php echo $fft_x[2];?></td>						
						<td><?php echo $fft_y[2];?></td>
					</tr>
					<?php } if (in_array('Frequency(Hz)',$tableContent)) {
						
					?>
					<tr>
						<th>Frequency (Hz)  </th>
						<td><?php  echo $fft_z[4];?></td>
						<td><?php echo $fft_x[4];?></td>						
						<td><?php echo $fft_y[4];?></td>
						
					</tr>
					<?php } if (in_array('PPV(mm/s)',$tableContent)) {
						
					 ?>
					<tr>
						<th>PPV (mm/s)  </th>
						<td><?php echo $fft_z[3];?></td>
						<td><?php echo $fft_x[3];?></td>						
						<td><?php echo $fft_y[3];?></td>
						
					</tr>
				    <?php } ?>
		</table>     
    <div class="chart">
		<h1>Transverse</h1>
		
        <div id="chartContainer1"  style="width:102%; margin-bottom:200%"></div>
    </div>
	<div class="chart">
		<h1>Radial</h1>
		
        <div id="chartContainer2"  style="width:102%;margin-bottom:104%;"></div>
    </div>

	<div class="chart" >
		<h1>Vertical</h1>
		
        <div id="chartContainer3"  style="width:102%; margin-bottom:64%;"></div>
    </div>
	<div style="clear:both;"></div>
	
	<div class="fft">
        <h1></h1>
        
		<div id="chartContainer4"  style="width:102%; margin-bottom:50%;"></div>
    </div>
    
   
</div>
   
<div style="clear:both;"></div>
<b><hr  class="chart" style="margin-right:0px;margin-left:350px;color:black;background-color:#333;height:2px;"></b>
  <script type="text/javascript">

	window.onload = function () {
		 setTimeout(function(){
					$('body').addClass('loaded');
				}, 1000);
				setTimeout(function(){
					$('.loader').fadeOut('400');
				}, 600);
		var chart = new CanvasJS.Chart("chartContainer1", {
			theme: "theme2",//theme1
			animationEnabled: false,
			zoomEnabled:true,
			axisY:{
				includeZero: false,
				title: "mm/s"
			},
			legend:{
				verticalAlign: "top",
				horizontalAlign: "left"

				
			},
			data: [              
			{
				type: "line",
				toolTipContent: "ZAxis: {y}", 
				showInLegend: true,
				lineThickness: 2,
				name: "Y-Axis",
				markerType: "square",
				color: "blue",
				dataPoints: [
					<?php print_r(implode(",",$transverse)); ?>       
				]
			}
			]
		});
		chart.render();
		
		var chart2 = new CanvasJS.Chart("chartContainer2", {
			theme: "theme2",//theme1
			animationEnabled: false,
			zoomEnabled:true,
			axisY:{
				includeZero: false,
				title: "mm/s"
			},
			legend:{
				verticalAlign: "top",
				horizontalAlign: "left"
			},
			data: [              
			{
				type: "line",
				showInLegend: true,
				toolTipContent: "X Axis: {y}",
				lineThickness: 2,
				name: "X-Axis",
				markerType: "square",
				color: "red",
				dataPoints: [<?php print_r(implode(",",$radial)); ?> ]
			}
			]
		});
		chart2.render();
		
		var chart3 = new CanvasJS.Chart("chartContainer3", {
			theme: "theme2",//theme1
			animationEnabled: false,
			zoomEnabled:true,
			axisY:{
				includeZero: false,
				title: "mm/s"
			},
			legend:{
				verticalAlign: "top",
				horizontalAlign: "left"
			},
			data: [              
			{
				type: "line",
				showInLegend: true,
				toolTipContent: "Y Axis: {y}",
				lineThickness: 2,
				name: "Z-Axis",
				markerType: "square",
				color: "green",
				dataPoints: [
					<?php print_r(implode(",",$vertical)); ?>      
				]
			}
			]
		});
		chart3.render();

		var chart4 = new CanvasJS.Chart("chartContainer4", {
			animationEnabled: false,
			axisY:{
				includeZero: false,
				title: "PPV(mm/s)"
			},
			axisY2:{
				includeZero:false,
				title:"Displacement(mm)",
				color:"purple"
				
			},
			axisX:{
				includeZero: false,
				title: "Hz"
			},
			
			legend:{
				verticalAlign: "bottom",
				horizontalAlign: "center"
			},
			data: [              
			{
				type: "scatter",
				showInLegend: true,
				axisYIndex: 0,
				color: "green",
				name:"Vertical PPV",
				toolTipContent: "Vertical PPV: {y}",
				dataPoints: [
					<?php echo $fft_vertical; ?>       
				]
			},
			{
				type: "scatter",
				showInLegend: true,
				axisYIndex: 0,
				legendMarkerType: "triangle",
				color: "red",
				name:"Radial PPV",
				toolTipContent: "Radial PPV: {y}",
				dataPoints: [
					<?php echo $fft_radial; ?>       
				]
			},
			{
				type: "scatter",
				showInLegend: true,
				axisYIndex: 0,
				color: "blue",
				legendMarkerType: "square",
				name:"Transverse PPV",
				toolTipContent: "Transverse PPV: {y}",
				dataPoints: [
					<?php echo $fft_transverse; ?>       
				]
			},
			{
				type: "scatter",
				showInLegend: true,
				axisYType: "secondary",
				color: "orange",
				legendMarkerType: "circle",
				name:"Z-Displacement",
				toolTipContent: "Z-Displacement: {y},Frequency:{x}",
				dataPoints: [
					<?php echo $displacement_z; ?>       
				]
			},
			{
				type: "scatter",
				showInLegend: true,
				
				color: "black",
				axisYType: "secondary",
				legendMarkerType: "triangle",
				toolTipContent: "X-Displacement: {y},Frequency:{x}",
				name:"X-Displacement",
				dataPoints: [
					<?php echo $displacementt_x; ?>       
				]
			},
			{
				type: "scatter",
				showInLegend: true,
				axisYType: "secondary",
				color: "purple",
				legendMarkerType: "square",
				toolTipContent: "Y-Displacement: {y},Frequency:{x}",
				name:"Y-Displacement",
				dataPoints: [
					<?php echo $displacement_y; ?>       
				]
			},
			
			{type: "line", 
                 showInLegend: true,
                 color:"black",
                 name: 'Bulding & Commercials',
                 markerSize: 0, 
                 dataPoints: [{x:1,y:20},
                              {x:10,y:20},
                              {x:50,y:40},
                              {x:100,y:50},
                              {x:110,y:50}]},
                 
                 {type: "line", 
                 showInLegend: true,
                 color:"orange",
                 name: 'Dwellings',
                 markerSize: 0, 
                 dataPoints: [{x:1,y:5},
                              {x:10,y:5},
                              {x:50,y:15},
                              {x:100,y:20},
                              {x:110,y:20}]},
                 {type: "line", 
                 showInLegend: true,
                 color:"red",
                 name: 'Preserved Bulding',
                 markerSize: 0, 
                 dataPoints: [{x:1,y:3},
                              {x:10,y:3},
                              {x:50,y:8},
                              {x:100,y:10},
                              {x:110,y:10}]}
			
			]
		});
		chart4.render();

		window.print();

		
		
		
	}
</script>
  <script src="js/canvasjs.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
        <script src="assets/plugins/jquery/jquery-2.2.0.min.js"></script>
        <script src="assets/plugins/materialize/js/materialize.min.js"></script>
        <script src="assets/plugins/material-preloader/js/materialPreloader.min.js"></script>
		<script src="assets/js/pages/form_elements.js"></script>
        <script src="assets/js/alpha.js"></script>


    </body>
    </html>
