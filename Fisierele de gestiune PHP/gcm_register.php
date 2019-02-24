<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['device_id']) && isset($_POST['token']) && isset($_POST['uid']) && $_POST['uid']!='') { 
 
    $device_id = $_POST['device_id'];
    $token = $_POST['token'];
    $uid = $_POST['uid'];
 
    // include db connect class
	require_once 'include/db_connect.php'; 
  
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE users SET token = '$token' WHERE device_id = $device_id AND uid = $uid");
 
    // check if row inserted or not
    if (mysql_affected_rows() != 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Token successfully updated to $token";
 
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