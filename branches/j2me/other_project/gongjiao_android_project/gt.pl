#!/usr/bin/perl
#
# for LSI msg summary
#
#
use Data::Dumper;
use Encode;





my @files =@ARGV;
#如果用户没有指定要解析的文件，默认使用test.h
if (scalar(@files) < 1) {
            push(@files,'test.h');
}
;





###########################################################
#开始处理待解析文件文件
###########################################################

my %db;
$state = 0;
$fsm = 0;
my @nodes;
my $node_num = 0;
my $link_id;
my $node_num = 0;
my $current_event_name;
my $all_link = 0;
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
open(MAN, "AndroidManifest.xml") or die "Opening strings.xml: $!";
open(ISTR, "strings.xml") or die "Opening strings.xml: $!";
open(IT, "Traffic.java") or die "Opening traffic.java: $!";
open(IM, "Model.java") or die "Opening Model.java.java: $!";

open(BP, "build.properties") or die "Opening build.properties: $!";
 undef $/;           # Turn off input record separator --
  my $str = <ISTR>; # read whole file as one string.
  my $man = <MAN>; # read whole file as one string.
  my $it = <IT>; # read whole file as one string.
  my $im = <IM>; # read whole file as one string.
  my $bp = <BP>; # read whole file as one string.
  close(MAN);
  close(ISTR);
  close(IT);
  close(IM);
  close(BP);
$/="\n";
my $OP;
my $dir;
#遍历需要解释的各个文件, we record all node info and edge info accordding the FSM array
foreach $file (@files) {
    print "Reading $file\n";
    if (!open(IN,$file)) { 
         print "Cannot open $file : $! \n" ;
         next; 
     }
    $node_num = 0;
    $current_event_name;
    @nodes=();
    $all_link = 0;
    while (<IN>) {
    	
    	
    	#如果匹配到END，标志着一个FSM的结束，打印出jpg文件
        if (/^\/\/#elif.*/o)  {
        	
          print $OP "\n}\n";
           close($OP);
            chdir "../../../../../" or die "cannot chdir to ../../../: $!";
        }
        # 以STATE_START开始,如果匹配到该标志, means that the function is below
        if (/\s*.*\s*currentCity==(traffic_.+)\s*$/o)
        {
         	$dir = $1;
         	$state = 1;
         	`cp -R base $1`;
         	`mkdir -p  $1/src/com/xianle/$1`;
         	
         	print $dir;
         	
         	chdir "$dir" or die "cannot chdir to $1/res/values/: $!";
         	
         	open($OP, ">AndroidManifest.xml") or die "Creating $file: $!";
         	$tempstr =$man;
         	$tempstr =~ s/aaaaa/$dir/m;
	        print $OP  $tempstr;
	        close($OP);
         	
         	open($OP, ">build.properties") or die "Creating $file: $!";
         	$tempstr =$bp;
         	$tempstr =~ s/aaaaa/$dir/m;
	        print $OP  $tempstr;
	        close($OP);
         	
         	
         	chdir "res/values" or die "cannot chdir to $1/res/values/: $!";
         	open($OP, ">strings.xml") or die "Creating $file: $!";
         	
         	$tempstr =Encode::decode("utf8", $str);
         	$re = Encode::decode("gb2312", $map{$dir}.'公交查询');
         	
                $tempstr =~ s/##/$re/m;
                $re = Encode::decode("gb2312", $map{$dir}.'公交');
                 $tempstr =~ s/aaaa/$re/m;
	        print $OP  encode("utf8",$tempstr);
	        close($OP);
	        #chdir "../../../" or die "cannot chdir to ../../../: $!";
	        
	        
	        $diraa="../../src/com/xianle/".$dir;
	        chdir  $diraa or die "cannot chdir to $1/src/com/xianle/$1: $!";
	        open($OP, ">Traffic.java") or die "Creating Traffic.java $!";
	        print $OP "package com.xianle.".$dir.";\n";
	        print $OP $it;
	        close($OP);
	        
	        
	        
	        open($OP, ">Model.java") or die "Creating Traffic.java $!";
	        print $OP "package com.xianle.".$dir.";\n";
	        print $OP $im;
	        
	    	print "Updated $dir\n";
	    	next;
         	 
        }
       if (/^\/\/#endif.*/o)  {
            print $OP "\n}\n";
            close($OP);
            chdir "../../../../../" or die "cannot chdir to ../../../: $!";
            
        }
   
        
        
		#record all node info
        if($state == 1) {
        	#$_ =Encode::decode("utf8", $_);
        print $OP $_;	
         
        }
        
	   

 
    }
    #end while (<IN>)
    close(IN);
}

 



