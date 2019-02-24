#!/bin/sh
sync=$(python sync.py)
hour=$sync/60
min=$sync-$hour*60
sudo python /home/pi/program.py
crontab -r
#write out current crontab
crontab -l > mycron
#echo new cron into cron file
echo "*/${min} */${hour} * * * /home/pi/job.sh" >> mycron
#install new cron file
crontab mycron
rm mycron
