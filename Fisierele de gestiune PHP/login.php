<?php
$con=mysqli_connect("localhost","cateisib_1","64Std(0b{w=x2Qg","cateisib_1");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$id = $_POST['deviceId'];
$password = $_POST['password'];
$result = mysqli_query($con,"SELECT name FROM devices where id='$id' and password='$password'");
$row = mysqli_fetch_array($result);
$data = $row[0];
if($data){
 echo $data;
}
mysqli_close($con);
?>