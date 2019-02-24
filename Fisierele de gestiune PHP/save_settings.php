<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
 
// array for JSON response 
$response = array();
 
// check for required fields
if (isset($_POST['device_id']) && isset($_POST['parameter']) && isset($_POST['parameter_value']) && isset($_POST['uid'])) {
 
    $device_id = $_POST['device_id'];
    $parameter = $_POST['parameter']; 
    $parameter_value = $_POST['parameter_value'];
    $uid = $_POST['uid'];
 
    // include db connect class 
	require_once 'include/db_connect.php';
  
    // connecting to db
    $db = new DB_CONNECT();
 
	if($parameter == "sync_frequency"){
		$result = mysql_query("UPDATE devices SET $parameter = $parameter_value WHERE id = $device_id");
    }
	else{
		$result = mysql_query("UPDATE users SET $parameter = $parameter_value WHERE device_id = $device_id AND uid = $uid");
	}
    // check if row inserted or not
    if (mysql_affected_rows() != 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Settings updated to $parameter"."UPDATE users SET $parameter = $parameter_value WHERE device_id = $device_id";
 
        // echoing JSON response 
        echo json_encode($response); 
    } else {
				// failed to insert row
				$response["success"] = 0;
				$response["message"] = "Oops! An error occurred.";
		 
				// echoing JSON response
				echo json_encode($response); 
			
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>