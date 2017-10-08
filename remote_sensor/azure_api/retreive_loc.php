<?php
   	include("connect.php");
	$link=Connection();

	if ($query = $link->prepare("SELECT * FROM latest_data;"))
	{
			if ($query->execute()){
				$query->bind_result(
					$sid, $latitude, $longitude, $level, $temp, $co, $smoke, $time);
			}
			else {
				die("Error: ". $link->error);
			}
			
			$result = array();
			while ($query->fetch()) {
				$result[] = array(
					'sensor_id' => $sid,
					'time' => $time,
					'co' => $co,
					'smoke' => $smoke,
					'temp' => $temp,
					'latitude' => $latitude,
					'longitude' => $longitude,
					'level' => $level
				);
			}
			echo json_encode($result);

			$query->close();
	}
	else {
		die("Error: ". $link->error);
	}
	mysqli_close($link);
?>
