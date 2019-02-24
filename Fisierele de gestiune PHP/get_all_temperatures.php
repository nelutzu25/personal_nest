<?php
 
/*
 * Following code will list all the temperatures
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'db_connect.php';
  
// connecting to db
$db = new DB_CONNECT();
 
// get all temperatures from temperatures table
$result = mysql_query("SELECT *FROM temperaturi") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results 
    // temperatures node
    $response["temperatures"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $temperature = array();
        $temperature["id"] = $row["id"];
        $temperature["date"] = $row["date"];
        $temperature["temperature"] = $row["temperature"];
		$temperature["deviceId"] = $row["deviceId"];
 
        // push single temperature into final response array
        array_push($response["temperatures"], $temperature);
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
?>