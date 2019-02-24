<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['device_id']) && isset($_POST['desired_temperature'])) {
 
    $device_id = $_POST['device_id'];
    $desired_temperature = $_POST['desired_temperature'];
 
    // include db connect class
	require_once 'include/db_connect.php';
  
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE desired_temperatures SET desired_temperature = '$desired_temperature' WHERE device_id = $device_id");
 
    // check if row inserted or not
    if (mysql_affected_rows() != 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Desired temperature successfully updated to $desired_temperature";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
		    // mysql inserting a new row
			$result = mysql_query("INSERT INTO desired_temperatures(desired_temperature, device_id) VALUES('$desired_temperature', '$device_id')");
		 
			// check if row inserted or not
			if ($result) {
				// successfully inserted into database
				$response["success"] = 1;
				$response["message"] = "Desired temperature successfully inserted.";
		 
				// echoing JSON response
				echo json_encode($response);
			} else {
				// failed to insert row
				$response["success"] = 0;
				$response["message"] = "Oops! An error occurred.";
		 
				// echoing JSON response
				echo json_encode($response); 
			}
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>