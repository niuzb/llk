#!/usr/bin/perl
use Encode;


$i = 0;


printf ROAD ("private void initRoadHash(){\n");

@gongjiao=qw(chongqing  guiyang
  hangzhou  nanchang  shenyang         taiyuan
fuzhou   haikou   kunming   nanjing   shijiazhuang   tianjing  xian);

%map = (
"chongqing" => "重庆", 
"guiyang" => "贵阳", 
"hangzhou" => "杭州", 
 "nanchang" => "南昌", 
 "shenyang" => "沈阳", 
 "taiyuan" => "太原", 
 "fuzhou" => "福州", 
 "haikou" => "海口", 
 "kunming" => "昆明", 
 "nanjing" => "南京", 
 "shijiazhuang" => "石家庄", 
 "tianjing" => "天津", 
  "xian" => "西安", 
               
                 
);
foreach $name(@gongjiao) {
#`rm -rf  traffic_$name/traffic/`;
print $map{$name},"公交查询\n";
#$file="traffic_$name/messages_cn.txt";
#if (!open(IN,$file)) { 
#         print "Cannot open $file : $! \n" ;
#         next; 
#     }
#select IN;

}

