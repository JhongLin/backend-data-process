<?php
    //print_r($_FILES);
    /*
        echo "Upload: " . $_FILES["userfile"]["name"] . "<br />";
        echo "Type: " . $_FILES["userfile"]["type"] . "<br />";
        echo "Size: " . ($_FILES["userfile"]["size"] / 1024) . " Kb<br />";
        echo "Temp file: " . $_FILES["userfile"]["tmp_name"] . "<br />";
    */
    //echo getenv('APACHE_RUN_USER');
    $uploaddir = realpath('./') . '/';
    #echo exec('whoami');
    if( isEvent($_FILES["userfile"]["name"]) > 0 ) # the case of EVENT file 
    {
        $type = "EVENT";
        $dirname = "event";
        $sensor = isEvent($_FILES["userfile"]["name"]);
    }
    else if( isPPV_Daily($_FILES["userfile"]["name"]) > 0 ) # the case of PPV_Daily file 
    {
        $type = "PPV_DAILY";
        $dirname = "rec/PPV_daily";
        $sensor = isPPV_Daily($_FILES["userfile"]["name"]);
    }
    else if( isDIN_Daily($_FILES["userfile"]["name"]) > 0 ) # the case of DIN_Daily file 
    {
        $type = "DIN_DAILY";
        $dirname = "rec/DIN_daily";
        $sensor = isDIN_Daily($_FILES["userfile"]["name"]);
    }
    else
        $type = "ERROR";
    $dir_abs_path = $uploaddir.$dirname."/".$sensor; # the target folder of saving file 
    $uploadfile = $uploaddir .$dirname."/". basename($_FILES['userfile']['name']); # select the upload file
    $rp = $dir_abs_path."/". basename($_FILES['userfile']['name']); # the final path of file
    
    if ($_FILES["userfile"]["error"] > 0)
    {
        echo "Return Code: " . $_FILES["userfile"]["error"] . "<br />";
    }
    else if($type == "ERROR")
        echo "Wrong file type! <br/>";
    else
    {   
        if(!is_dir($dir_abs_path))
        {
            //mkdir($dir_abs_path, 0755);
            if (!mkdir($dir_abs_path, 0755, true)) {
                die('Failed to create folders...');
            }
        }
        if (file_exists($dir_abs_path."/" . $_FILES["userfile"]["name"]))
        {   
            if($type == "PPV_DAILY") //PPV_Daily needs update frequently
            {
                if (move_uploaded_file($_FILES['userfile']['tmp_name'], $rp)) 
                {
                    echo "File is valid, and was successfully uploaded. ";
                    echo "Stored in: " .$dirname. "/" . $_FILES["userfile"]["name"] . "<br />";
                }
                else
                {
                    echo "Possible file upload attack! at ".$_FILES["userfile"]["name"]."<br/>";
                }
            }
            else if($type == "DIN_DAILY")
            {
                if (move_uploaded_file($_FILES['userfile']['tmp_name'], $rp)) 
                {
                    echo "File is valid, and was successfully uploaded. ";
                    echo "Stored in: " .$dirname. "/" . $_FILES["userfile"]["name"] . "<br />";
                }
                else
                {
                    echo "Possible file upload attack! at ".$_FILES["userfile"]["name"]."<br/>";
                }
            }
            else
                echo $dirname. "/" .$_FILES["userfile"]["name"] . " already exists." . "<br />";
        }
        else
        {   
            if (move_uploaded_file($_FILES['userfile']['tmp_name'], $rp)) 
            {
                echo "File is valid, and was successfully uploaded. ";
                echo "Stored in: " .$dirname. "/" . $_FILES["userfile"]["name"] . "<br />";
            } 
            else
            {
                echo "Possible file upload attack! at ".$_FILES["userfile"]["name"]."<br/>";
            }
        }
    }

    function isEvent($name)
    {
        $fne = explode('.', $name);
        $type_name = explode('_', $name);
        if($fne[count($fne)-1] == "csv")
            if($type_name[3] == "dinEvent")
                return $type_name[2];
        return 0;
    }
    function isPPV_Daily($name)
    {
        $fne = explode('.', $name);
        $type_name = explode('_', $name);
        if($fne[count($fne)-1] == "csv")
            if($type_name[3] == "CEP.csv")
                return $type_name[1];
        return 0;
    }
    function isDIN_Daily($name)
    {
        $fne = explode('.', $name);
        $type_name = explode('_', $name);
        if($fne[count($fne)-1] == "csv")
            if($type_name[3] == "dinDaily")
                return $type_name[2];
        return 0;
    }
?>