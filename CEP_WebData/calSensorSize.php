<?php 
    function findSensorDir($path, $sensor) // calculate the capacity usage of given path
    {   
        $dirs = scandir($path);
        $paths = array();
        foreach($dirs as $key=>$value)
        {   
            if(is_dir($path."/".$value))
            {
                if($value == '.' || $value == "..")
                    continue;
                if($value == $sensor)
                {
                    $paths[] = $path."/".$value;
                }
                else
                {
                    $r_path = findSensorDir($path."/".$value, $sensor);
                    foreach($r_path as $index=>$sensorPath)
                    {
                        $paths[] = $sensorPath;
                    }
                }
                //print("it's a Dir.");
                //echo "<br/>";
            }
        }
        return $paths;
    }
    $sensor_usage = array();
    $file = fopen("sensorSize.json","w");
    $current_path = realpath('./');
    $ppv_path = $current_path."/rec/PPV_daily";
    $dirs = scandir($ppv_path);
    foreach($dirs as $key=>$value)
    {   
        if($value == '.' ||$value == "..")
            continue;
        $sensor_paths = findSensorDir($current_path, $value);
        $size = 0.0;
        foreach($sensor_paths as $index=>$sensorPath)
        {   
            $du_str =  exec("du -s ".$sensorPath);
            $size_str = explode('\t', $du_str);
            $size += (float)$size_str[0];
        }
        //$size = (string)$size;
        $sensor_usage[$value] = $size;
    }
    $sensor_usage["timestamp"] = time();
    //echo '<pre>', print_r($sensor_usage), '</pre>';
    $json_string = json_encode($sensor_usage, JSON_PRETTY_PRINT);
    fwrite($file, $json_string);
    fclose($file);
    echo $json_string;
?>