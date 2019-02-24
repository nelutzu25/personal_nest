<?php
 
/*
 * Following code will list all the temperatures
 */
 
// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['device_id']) && isset($_POST['history_days'])) {
 
    $device_id = $_POST['device_id'];
    $history_days = $_POST['history_days'];
 
	 // include db connect class
	require_once 'include/db_connect.php';
	  
	// connecting to db
	$db = new DB_CONNECT();
	 
	// get all parameters from parameters table
	$result = mysql_query("select * from parameters
							where date between date_sub(now(),INTERVAL $history_days DAY) and now()
							and device_id = $device_id
							ORDER BY date ASC") 
			or die(mysql_error()); 
	 
	// check for empty result 
	if (mysql_num_rows($result) > 0) { 
		// looping through all results 
		// temperatures node
		$response["parameters"] = array();
	  
		while ($row = mysql_fetch_array($result)) {
			// temp user array
			$parameter = array();
			//$parameter["id"] = $row["id"];
			$parameter["date"] = $row["date"];
			$parameter["temperature"] = $row["temperature"];
			$parameter["humidity"] = $row["humidity"];
			//$parameter["device_id"] = $row["device_id"];
	 
			// push single temperature into final response array
			array_push($response["parameters"], $parameter);
		}
		
		// success
		$response["success"] = 1;
	 
		// echoing JSON response
		echo json_encode($response);
	} else { 
		// no temperatures found
		$response["success"] = 0;
		$response["message"] = "No temperatures found";
	 
		// echo no users JSON
		echo json_encode($response);
	}
}
else{
	    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>