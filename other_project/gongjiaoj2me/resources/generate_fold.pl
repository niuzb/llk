#!/usr/bin/perl
use Encode;


$i = 0;


printf ROAD ("private void initRoadHash(){\n");

@gongjiao=qw(chongqing  guiyang
  hangzhou  nanchang  shenyang         taiyuan
fuzhou   haikou   kunming   nanjing   shijiazhuang   tianjing  xian);

%map = (
"chongqing" => "����", 
"guiyang" => "����", 
"hangzhou" => "����", 
 "nanchang" => "�ϲ�", 
 "shenyang" => "����", 
 "taiyuan" => "̫ԭ", 
 "fuzhou" => "����", 
 "haikou" => "����", 
 "kunming" => "����", 
 "nanjing" => "�Ͼ�", 
 "shijiazhuang" => "ʯ��ׯ", 
 "tianjing" => "���", 
  "xian" => "����", 
               
                 
);
foreach $name(@gongjiao) {
#`rm -rf  traffic_$name/traffic/`;
print $map{$name},"������ѯ\n";
#$file="traffic_$name/messages_cn.txt";
#if (!open(IN,$file)) { 
#         print "Cannot open $file : $! \n" ;
#         next; 
#     }
#select IN;

}

