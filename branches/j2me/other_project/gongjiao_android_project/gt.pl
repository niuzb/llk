#!/usr/bin/perl
#
# for LSI msg summary
#
#
use Data::Dumper;
use Encode;





my @files =@ARGV;
#����û�û��ָ��Ҫ�������ļ���Ĭ��ʹ��test.h
if (scalar(@files) < 1) {
            push(@files,'test.h');
}
;





###########################################################
#��ʼ����������ļ��ļ�
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
#������Ҫ���͵ĸ����ļ�, we record all node info and edge info accordding the FSM array
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
    	
    	
    	#���ƥ�䵽END����־��һ��FSM�Ľ�������ӡ��jpg�ļ�
        if (/^\/\/#elif.*/o)  {
        	
          print $OP "\n}\n";
           close($OP);
            chdir "../../../../../" or die "cannot chdir to ../../../: $!";
        }
        # ��STATE_START��ʼ,���ƥ�䵽�ñ�־, means that the function is below
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
         	$re = Encode::decode("gb2312", $map{$dir}.'������ѯ');
         	
                $tempstr =~ s/##/$re/m;
                $re = Encode::decode("gb2312", $map{$dir}.'����');
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

 



