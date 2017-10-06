<?php
   	include("connect.php");
	$link=Connection();

	$sid=$_POST["sensor_id"];
	$temp=$_POST["temp"];
	$co=$_POST["co"];
	$smoke=$_POST["smoke"];

	if ($query = $link->prepare(
		"INSERT INTO `measurements` (`s_id`, `temp`, `co`, `smoke`) 
		VALUES (?, ?, ?, ?)")){
			$query->bind_param("idii", 
				$sid, $temp, $co, $smoke);
			$query->execute();
			echo "Successfully added.";
			$query->close();
	}
	else {
		die("Error: ". $link->error);
	}
	mysqli_close($link);
?>
