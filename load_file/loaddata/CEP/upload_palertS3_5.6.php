<?php
    
    function run(){
        $temp=get_quota();
        $sensor=$temp[1];
        $quota=$temp[0];
        $usage=get_project_usage($sensor);

        $current_path =  realpath('./'); //to get current path of this php

        $dirs = array_filter(glob('*'), 'is_dir'); // scan the folders of current path

        foreach($dirs as $key => $value) // scan the files of every folder
        {   
            if($value == "data_backup") // ignore the backup directory
                continue;
        	$upload_dir_name = $value."/";
        	$from_path = $current_path.'/'.$upload_dir_name; // record the upload path

            if(is_dir($from_path))
            {
                //echo "Find this Folder! <br />";
                $files = scandir($from_path, 1);
                //echo '<pre>', print_r($files), '</pre>';
                foreach($files as $index => $file_name) // upload each file if it's EVENT/Daily
                {
                    $sensor_num="";
                    if(isEvent($file_name))// ||isDIN_Daily($file_name)
                    {   
                        $sensor_num=explode('_',$file_name)[2];
                        //echo($size_str.'<br>');
                        //echo print($file_name)."<br />";
                        //upload($file_name, $upload_dir_name); //use relative path
                    }
                    else if(isPPV_Daily($file_name))
                    {
                        $sensor_num=explode('_',$file_name)[1];
                    }
                    if($sensor_num!="")
                    {
                        $du_str =  exec("du ".$from_path.$file_name);
                        $size =(float) explode('\t', $du_str)[0];
                        //echo "size: ",$size."<br>";
                        $project=$sensor[$sensor_num];
                        $project_quota=$quota[$project];
                        $project_usage=$usage[$project];
                        //echo "project_usage: ",$project_usage."<br>";
                        $size_=$project_usage+$size;
                        //echo($size_."<br>");
                        if($size_<$project_quota)
                        {
                            //upload($file_name, $upload_dir_name);
                            $usage[$project]=$size_;
                            upload($file_name, $upload_dir_name);
                        }
        
                    }
                }
            }
            else
            {
                echo "No such a Folder! <br />";
            }
        }
    }
    run();
    /*function test()
    {
        $temp=get_quota();
        $sensor=$temp[1];
        $quota=$temp[0];
        get_project_usage($sensor);
    }*/

    function get_usage(){
        $url="cep.sanlien.com/CEP_WebData/calSensorSize.php";
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $usage = json_decode(curl_exec($ch),true);
        //print_r ($usage);
        $usage=array_map("floatval",$usage);
        return $usage;
    }
    
    function get_project_usage($sensor){
        
        $sensor_usage=get_usage();
        $project_usage=[];
        foreach($sensor as $sensor_num => $project)
        {   
            if(!array_key_exists($sensor_num,$sensor_usage))
                $sensor_usage[$sensor_num]=0;
            if(array_key_exists($project,$project_usage))
                $project_usage[$project]+=$sensor_usage[$sensor_num];
            else
                $project_usage[$project]=$sensor_usage[$sensor_num];
        }
        //print_r($project_usage);
        //echo"<br>";
        return $project_usage;
    }

    function get_quota(){

        $dbhost = "localhost";
        $dbuser = "cep";
        $dbpass = "********";
        $dbname = "cep";
        $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
        mysql_select_db($dbname,$conn);
        $sql="SELECT project.project_id, sensor.sensor_id, project.quota FROM project INNER JOIN sensor ON project.project_id = sensor.project_id";
        /*include ('model/model.php');
        $project = new Project();
        $result = $project->custosql($sql);*/
        $result=mysql_query($sql);
        $quota=array();
        $sensor=array();
        while($row=mysql_fetch_assoc($result))
        {
            //print_r($row);

            $quota[$row['project_id']]=((float)$row['quota'])*1024*1024;
            $sensor[$row['sensor_id']]=$row['project_id'];

        }


        /*print_r($quota);
        echo("<br>");
        print_r($sensor);*/
        return [$quota,$sensor];

    }

	function upload($name,$dir){
		//$toURL = "http://125.227.41.131/CEP/accpet.php";
		//$toURL = "http://localhost/accpet.php";
		$toURL = "http://10.10.10.67/CEP_WebData/accpet.php"; // upload target site
		$tmp=$dir;
		$dir="/".$dir."/";
		$post = array(
			//"name"=>"123",
			"userfile"=> '@'.realpath('./').$dir.$name
		);
		$ch = curl_init(); // setup curl upload
		
		curl_setopt($ch, CURLOPT_URL, $toURL);
		curl_setopt($ch, CURLOPT_POST, 1);
		//curl_setopt($ch, CURLOPT_SAFE_UPLOAD, false); // required as of PHP 5.6.0
		curl_setopt($ch, CURLOPT_POSTFIELDS, $post);
		
		/*
		$options = array(
			CURLOPT_URL=>$toURL,
			CURLOPT_SAFE_UPLOAD=> false,
			CURLOPT_POST=> 1,
			CURLOPT_POSTFIELDS=>$post, // 直接給array
		);
		curl_setopt_array($ch, $options);
		*/
		curl_exec($ch);
		curl_close($ch);
        $dir=$tmp."/";
		#unlink($dir.$name);
	}
	function isEvent($name) //check whether it's a EVENT FILE
    {
        $fne = explode('.', $name);
        $type_name = explode('_', $name);
        if($fne[count($fne)-1] == "csv" && count($type_name)>3)
            if($type_name[3] == "dinEvent")
                return true;
        return false;
    }
    function isPPV_Daily($name) //check whether it's a PPV_Daily FILE
    {
        $fne = explode('.', $name);
        $type_name = explode('_', $name);
        if($fne[count($fne)-1] == "csv" && count($type_name)>3)
            if($type_name[count($type_name)-1] == "CEP.csv")
                return true;
        return false;
    }
    function isDIN_Daily($name) //check whether it's a DIN_Daily FILE
    {
        $fne = explode('.', $name);
        $type_name = explode('_', $name);
        if($fne[count($fne)-1] == "csv" && count($type_name)>3)
            if($type_name[3] == "dinDaily")
                return true;
        return false;
    }
?>
