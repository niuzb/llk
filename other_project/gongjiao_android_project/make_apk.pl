%map = (
"traffic_bj"  => "����",     
"traffic_sz"  => "����",           
"traffic_sh" => "�Ϻ�",           
"traffic_guangzhou" => "����",
"traffic_zhengzhou" => "֣��",
"traffic_chengdu"  => "�ɶ�",
"traffic_dalian"  => "����",
"traffic_haerbin"  => "������",
"traffic_changsha" => "��ɳ",
"traffic_wuhan"  => "�人",
"traffic_hefei" => "�Ϸ�",
"traffic_nanning"  => "����",
"traffic_chongqing" => "����", 
"traffic_guiyang" => "����", 
"traffic_hangzhou" => "����", 
 "traffic_nanchang" => "�ϲ�", 
 "traffic_shenyang" => "����", 
 "traffic_taiyuan" => "̫ԭ", 
 "traffic_fuzhou" => "����", 
 "traffic_haikou" => "����", 
 "traffic_kunming" => "����", 
 "traffic_nanjing" => "�Ͼ�", 
 "traffic_shijiazhuang" => "ʯ��ׯ", 
 "traffic_tianjing" => "���", 
  "traffic_xian" => "����"
       
                 
);

foreach $file (keys(%map)) {
	print $file, "\n";
	#chdir "$file/bin" or die "cannot chdir to /res/values/: $!";
	#`ant debug`;
	#`ant release`;
	#`mv traffic-debug.apk ../../$file-debug.apk`;
	#`mv traffic-unsigned.apk ../../$file-release.apk`;
	`adb uninstall com.xianle.$file`;
	#chdir "../../" or die "cannot chdir to /values/: $!";
	} 