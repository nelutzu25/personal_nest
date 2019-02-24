<?php
// include db connect class
require_once 'include/db_connect.php';
//Generic php function to send GCM push notification
function sendMessageThroughGCM($registration_ids, $message) { 
	//Google cloud messaging GCM-API url
	$url = 'https://android.googleapis.com/gcm/send'; 
	$fields = array(
		'registration_ids' => $registration_ids, 
		'data' => $message,
	); 
	// Update your Google Cloud Messaging API Key
	define("GOOGLE_API_KEY", "AIzaSyCQzFLZN2wG11ZbRikzVCSavPOSFMBCxyc"); 	 	    
	$headers = array(
		'Authorization: key=' . GOOGLE_API_KEY,
		'Content-Type: application/json'
	);
	$ch = curl_init(); 
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);	
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));  
	$result = curl_exec($ch);				
	if ($result === FALSE) {  
		die('Curl failed: ' . curl_error($ch));
	}
	curl_close($ch);
	return $result;
}
	
// array for JSON response
$response = array();
 
// check for required fields 
if (isset($_POST['device_id']) && isset($_POST['current_temperature']) && isset($_POST['current_humidity']) && isset($_POST['date'])) {
 
    $device_id = $_POST['device_id'];
    $temperature = $_POST['current_temperature'];
    $humidity = $_POST['current_humidity'];
    $date = $_POST['date'];
   
    // connecting to db
    $db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO parameters(temperature, humidity, date, device_id) VALUES($temperature, $humidity, '$date', $device_id)");						
	
	$sql = mysql_query("SELECT * FROM users WHERE device_id = $device_id");  
	//echo "SELECT * FROM users WHERE device_id = $device_id";
	while($row = mysql_fetch_array($sql)){
		if($row['notification'] != false || $row['notification'] == 1){
		
			$gcmRegIds = array();
			$message = "";
	 
			array_push($gcmRegIds,$row['token']);
			 
			if($temperature > $row['notify_max_temp']){ 
				$message = "Temperature is above ".$row['notify_max_temp']." degrees Celsius\n";
			}
			if($temperature < $row['notify_min_temp']){
				$message .= "Temperature is below ".$row['notify_min_temp']." degrees Celsius\n"; 
			}
			if($humidity > $row['notify_max_humidity']){
				$message .= "Humidity is above ".$row['notify_max_humidity']."% \n"; 
			}  
			if($humidity < $row['notify_min_humidity']){  
				$message .= "Humidity is below ".$row['notify_min_humidity']."% \n";
			}
			$temperature = round($temperature,2);
			$humidity = round($humidity,2);
			$message .= "Current temperature: ".$temperature." degrees Celsius \n";
			$message .= "Current humidity: ".$humidity."% \n";
			
			$pushmessage = array("message" => $message);	
			$pushStatus = sendMessageThroughGCM($gcmRegIds, $pushmessage);  
		}
	}
	

	$sql = mysql_query("SELECT desired_temperature FROM desired_temperatures WHERE device_id = $device_id");
	$row = mysql_fetch_array($sql);	
	$response = $row['desired_temperature'];
	echo $response; 	
}
 else {
    echo 0;
}
?>