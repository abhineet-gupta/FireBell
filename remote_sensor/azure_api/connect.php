<?php

	function Connection(){
		$server="localhost";
		$user="abhi	";
		$pass="F1rebelldb";
		$db="firebell";
	   	
		$connection = mysqli_connect($server, $user, $pass, $db);

		if (!$connection) {
	    	die('MySQL ERROR: ' . mysql_error());
		}
		else {
			echo "Connected successfully.";
		}

		return $connection;
	}
?>
