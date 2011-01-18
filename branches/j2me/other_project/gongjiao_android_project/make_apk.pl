%map = (
"traffic_bj"  => "北京",     
"traffic_sz"  => "深圳",           
"traffic_sh" => "上海",           
"traffic_guangzhou" => "广州",
"traffic_zhengzhou" => "郑州",
"traffic_chengdu"  => "成都",
"traffic_dalian"  => "大连",
"traffic_haerbin"  => "哈尔滨",
"traffic_changsha" => "长沙",
"traffic_wuhan"  => "武汉",
"traffic_hefei" => "合肥",
"traffic_nanning"  => "南宁",
"traffic_chongqing" => "重庆", 
"traffic_guiyang" => "贵阳", 
"traffic_hangzhou" => "杭州", 
 "traffic_nanchang" => "南昌", 
 "traffic_shenyang" => "沈阳", 
 "traffic_taiyuan" => "太原", 
 "traffic_fuzhou" => "福州", 
 "traffic_haikou" => "海口", 
 "traffic_kunming" => "昆明", 
 "traffic_nanjing" => "南京", 
 "traffic_shijiazhuang" => "石家庄", 
 "traffic_tianjing" => "天津", 
  "traffic_xian" => "西安"
       
                 
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