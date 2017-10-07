<?php
   	include("connect.php");
	$link=Connection();

	$sid=$_POST["sensor_id"];
	$limit=$_POST["limit"];

	if ($query = $link->prepare
			(
			"SELECT `time`, `temp`, `co`, `smoke` 
				FROM measurements 
				WHERE s_id = ? 
				ORDER BY m_id DESC LIMIT ?;"
			)
		)
	{
			$query->bind_param("ii", $sid, $limit);
			if ($query->execute()){
				$query->bind_result($time, $temp, $co, $smoke);
			}
			else {
				die("Error: ". $link->error);
			}

			while ($query->fetch()) {
				$result = array();
				$result['time'] = $time;
				$result['co'] = $co;
				$result['temp'] = $temp;
				$result['smoke'] = $smoke;
				$result['sensor_id'] = $sid;

				echo json_encode($result);
			}

			$query->close();
	}
	else {
		die("Error: ". $link->error);
	}
	mysqli_close($link);
?>
