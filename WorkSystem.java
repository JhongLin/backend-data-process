import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.text.DecimalFormat;
import java.io.PrintWriter;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.io.Console;

public class WorkSystem{
	static Scanner jku=new Scanner(System.in);
	static int wt;
	static String ts;
	static int i,j,k,l;//迴圈用
	static boolean root=false;
	static boolean[] dr=new boolean[8];//display right
	
	
    static Console cnsl=System.console();//hide entered password
    static char[] hpa;
	
	static FileReader fr;
	static FileReader fr2;
	
	static BufferedReader br;
	static BufferedReader br2;
	
	static void IOE()throws IOException{
		fr= new FileReader("Account.txt");
		fr2= new FileReader("Account.txt");
		br = new BufferedReader(fr);
		br2 = new BufferedReader(fr2);
	}
	static StringTokenizer tk;
	//************************WORKS DATA**************************************
	static FileReader frd;
	static FileReader frd2;
	
	static BufferedReader brd;
	static BufferedReader brd2;
	
	static void IOEd()throws IOException{
		frd= new FileReader("works.txt");
		frd2= new FileReader("works.txt");
		brd = new BufferedReader(frd);
		brd2 = new BufferedReader(frd2);
	}
	
	static int rnd=0;//rows number of data
	
	static String[] ord;//原本每一行的樣子
	
	static String[][] work;
	
	static void countLined()throws IOException{
		while((ts=brd.readLine())!=null) //算出總共幾行
			rnd++;

		work=new String[rnd][8];
		ord=new String[rnd];
		}
	
	static void setOrd()throws IOException{
		for(i=0;i<rnd;i++)
			ord[i]=brd2.readLine();
	}
	
	static void setWork(){
		for(i=0;i<rnd;i++){//裝入工作內容
			tk= new StringTokenizer(ord[i]," ");
			for(j=0;j<8;j++)
				work[i][j]=tk.nextToken();
		}//END裝填內容
	}
	
	
	
	
	
	
	static public void createWork(){
		String Title,No,content;
		String[] CG={"日常","娛樂","工作","其它"};
		int year,month,day,pr,lar,CGcode;
		int fyear,fmonth,fday;
		System.out.print("Enter the Work Title(less than 24 bits):");
		Title=jku.next();
		for(i=0;i<work.length;i++){
			if(Title.equals(work[i][1])){
				System.out.println("The Work Title had existed!");
				System.out.println("Please try again.");
				System.out.print("Press enter to continue....");
				Scanner input = new Scanner(System.in); 
				input.hasNextLine();
				createWork();return;
			}
		}//end repeat error occur
		System.out.println("Please enter the Date when the work started.");
		System.out.println("If work hasn't started, please enter 0");
		System.out.print("Please enter the year first(XXXX):");
		year=jku.nextInt();
		if(year!=0){
			while(year<0||year>9999){
				System.out.print("Input is illegal!\nPlease enter again:");
				year=jku.nextInt();
			}
			System.out.print("Please enter the month next(XX):");
			month=jku.nextInt();
			while(month<0||month>12){
				System.out.print("Input is illegal!\nPlease enter again:");
				month=jku.nextInt();
			}
			System.out.print("Please enter the day last(XX):");
			day=jku.nextInt();
			while(day<0||day>31){
				System.out.print("Input is illegal!\nPlease enter again:");
				day=jku.nextInt();
			}
			System.out.print("Please enter the percentage of the work(0~100):");
			pr=jku.nextInt();
			while(pr<0||pr>100){
				System.out.print("Input is illegal!\nPlease enter again:");
				pr=jku.nextInt();
			}
		if(pr==100){//set Finish date
			System.out.println("Please enter the Date when the work finished.");
			System.out.print("Please enter the year first(XXXX):");
			fyear=jku.nextInt();
			while(fyear<0||fyear>9999){
				System.out.print("Input is illegal!\nPlease enter again:");
				fyear=jku.nextInt();
			}
			System.out.print("Please enter the month next(XX):");
			fmonth=jku.nextInt();
			while(fmonth<0||fmonth>12){
				System.out.print("Input is illegal!\nPlease enter again:");
				fmonth=jku.nextInt();
			}
			System.out.print("Please enter the day last(XX):");
			fday=jku.nextInt();
			while(fday<0||fday>31){
				System.out.print("Input is illegal!\nPlease enter again:");
				fday=jku.nextInt();
			}
		}//end of set Finish date
		else{
			fyear=0;fday=0;fmonth=0;
		}
		}
		else{
			pr=0;
			year=0;month=0;day=0;
			fyear=0;fmonth=0;fday=0;
		}
		for(i=0,lar=0;i<work.length;i++)
			if(Integer.valueOf(work[i][5].substring(1))>lar)
				lar=Integer.valueOf(work[i][5].substring(1));
		lar++;
		System.out.println("Please enter the category code.");
		System.out.println("日常( 0 )\n娛樂( 1 )\n工作( 2 )\n其它( 3 )");	
		CGcode=jku.nextInt();
		while(CGcode<0||CGcode>3){
			System.out.print("Input is illegal!\nPlease enter again:");
			CGcode=jku.nextInt();
		}
		System.out.println("Please describe the content of the work.");
		System.out.print("(it couldn't include any blank):");
		content=jku.next();
		
		String[][] temp=new String[work.length+1][8];
		for(i=0;i<work.length;i++)
			for(j=0;j<8;j++)
				temp[i][j]=work[i][j];
		temp[work.length][0]=Title;
		if(year==0)
			temp[work.length][2]="0000.00.00";
		else
			temp[work.length][2]=String.format("%04d.%02d.%02d", year,month,day);
		
		if(fyear==0)
			temp[work.length][1]="9999.99.99";
		else 
			temp[work.length][1]=String.format("%04d.%02d.%02d", fyear,fmonth,fday);
		
		temp[work.length][3]=Integer.toString(pr);
		if(pr==0)
			temp[work.length][4]="未開始";
		else if(pr==100)
			temp[work.length][4]="已完成";
		else
			temp[work.length][4]="執行中";
		
		temp[work.length][5]="j"+String.format("%05d", lar);
		
		if(CGcode==0)
		temp[work.length][6]="日常";
		else if(CGcode==1)
			temp[work.length][6]="娛樂";
		else if(CGcode==2)
			temp[work.length][6]="工作";
		else
			temp[work.length][6]="其它";
		temp[work.length][7]=content;
		
		work=temp;
		
		System.out.println("The work has created sucessfully!");
		
	}//end create
	
	static public void searchWork(){
		for(;;){
			System.out.println("\n\nWhat do you want to use to search?");
			System.out.println("Title(T)");
			System.out.println("Number(NO)");

			System.out.println("\nBack to Work interface(B)");
			System.out.println("Exit System(E)");
			String uc=jku.next(),nt,cgc;
			if(uc.equalsIgnoreCase("T")){
				System.out.print("Please enter the Title:");
				nt=jku.next();
				for(i=0;i<work.length;i++){
					if(work[i][0].equalsIgnoreCase(nt)){
						wprint(i);
						
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
						break;
					}
					
					else if(i==work.length-1){
						System.out.println("The Title doesn't exist!");
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
						break;
					}
						
				}

			}
			else if(uc.equalsIgnoreCase("NO")){
				System.out.print("Please enter the code number:");
				cgc=jku.next();
				for(i=0;i<work.length;i++){
					if(work[i][5].equalsIgnoreCase(cgc)){
						wprint(i);
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
						break;
					}
					else if(i==work.length-1){
						System.out.println("The Number doesn't exist!");
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
						break;
					}
				}
			}
			else if(uc.equalsIgnoreCase("B")){
				break;
			}
			else if(uc.equalsIgnoreCase("E")){
				stAc();
				stWd();
				System.exit(0);
			}
			else{
				System.out.println("Input is illegal!\nPlease try again.");
				System.out.println("The Number doesn't exist!");
				System.out.print("Press enter to continue....");
				Scanner input = new Scanner(System.in); 
				input.hasNextLine();
				continue;
			}
			
		}//end search interface
	}//end search
	
	static void wprint(int kk){
		if(dr[0])
			System.out.printf("%-25s",work[kk][0]);
		if(dr[1])
			System.out.printf("%-13s",work[kk][1]);
		if(dr[2])
			System.out.printf("%-13s",work[kk][2]);
		if(dr[3])
			System.out.printf("%-6s",work[kk][3]);
		if(dr[4])
			System.out.printf("%-8s",work[kk][4]);
		if(dr[5])
			System.out.printf("%-9s",work[kk][5]);
		if(dr[6])
			System.out.printf("%-7s",work[kk][6]);
		if(dr[7])
			System.out.printf("%s",work[kk][7]);
		System.out.println();
	}
	
	static void drSwitch(){
		if(root){
			for(;;){
				System.out.println("\n\nAdmin, what do you want to do?");
				System.out.println("Allow display rights(AR)");
				System.out.println("Disallow display rights(DR)");
				System.out.println("\nBack to Work interface(B)");
				System.out.println("Exit System(E)");
				String uc=jku.next();
				if(uc.equalsIgnoreCase("AR")){
					System.out.println("Please enter the element.");
					System.out.println("Title(0)    FDAY(1)\nSDAY(2)     PR(3)\nStatus(4)   No(5)\nCG(6)       Content(7)");
					int di=jku.nextInt();
					while(di<0||di>7){
						System.out.print("Input is illegal!\nPlease enter again:");
						di=jku.nextInt();
					}
					dr[di]=true;
				}
				else if(uc.equalsIgnoreCase("DR")){
					System.out.println("Please enter the element.");
					System.out.println("Title(0)    FDAY(1)\nSDAY(2)     PR(3)\nStatus(4)   No(5)\nCG(6)       Content(7)");
					int di=jku.nextInt();
					dr[di]=false;
				}
				else if (uc.equalsIgnoreCase("B"))
					break;
				else if (uc.equalsIgnoreCase("E")){
					stAc();
					stWd();
					System.exit(0);
				}
				else{
					System.out.println("Input is illegal!\nPlease try again.");
					System.out.println("The Number doesn't exist!");
					System.out.print("Press enter to continue....");
					Scanner input = new Scanner(System.in); 
					input.hasNextLine();
					continue;
				}
				
				
				
				
				
				
			}
		}
		else{
			System.out.println("You are not admin!");
		}
	}
	
	
	static public void printWork(){
		for(;;){
			System.out.println("\n\nNow is at print interface.\nWhat do you want to do?");
			System.out.println("Print all works(PW)");
			System.out.println("Print works appointed(PA)");
			
			System.out.println("\nBack to Work interface(B)");
			System.out.println("Exit System(E)");
			
			String uc=jku.next();
			if(uc.equalsIgnoreCase("PW")){
				for(i=0;i<work.length;i++){
				wprint(i);
				if(i%7==0&&i!=0){
					System.out.println("----------------------------------------------------------------------------------------------------");
					System.out.print("Press enter to print next page....");
					Scanner input = new Scanner(System.in); 
					input.hasNextLine();
					System.out.println("----------------------------------------------------------------------------------------------------");
				}}
				System.out.print("Press enter to continue....");
				Scanner input = new Scanner(System.in); 
				input.hasNextLine();
			}
			else if (uc.equalsIgnoreCase("PA")){

				System.out.println("Please enter which type you want to assign.\n");
				System.out.println("Status(4)\nCategory(6)");
				int iuc=jku.nextInt();
				if(iuc==4){
					System.out.println("執行中(0)\n已完成(1)\n未開始(2)");
					iuc=jku.nextInt();
					while(iuc<0||iuc>2){
						System.out.print("Input is illegal!\nPlease enter again:");
						iuc=jku.nextInt();
					}
					if(iuc==0){
						for(i=0;i<work.length;i++){
							if(work[i][4].equals("執行中"))
								wprint(i);
							
						}
					}
					else if(iuc==1){
						for(i=0;i<work.length;i++){
							if(work[i][4].equals("已完成"))
								wprint(i);
						}
					}
					else if(iuc==2){
						for(i=0;i<work.length;i++){
							if(work[i][4].equals("未開始"))
								wprint(i);
						}
					}
					
					
					
				}
				else if(iuc==6){
					
					System.out.println("日常(0)\n娛樂(1)\n工作(2)\n其它(3)");
					iuc=jku.nextInt();
					while(iuc<0||iuc>3){
						System.out.print("Input is illegal!\nPlease enter again:");
						iuc=jku.nextInt();
					}
					if(iuc==0){
						for(i=0;i<work.length;i++){
							if(work[i][6].equals("日常"))
								wprint(i);
						}
					}
					else if(iuc==1){
						for(i=0;i<work.length;i++){
							if(work[i][6].equals("娛樂"))
								wprint(i);
						}
					}
					else if(iuc==2){
						for(i=0;i<work.length;i++){
							if(work[i][6].equals("工作"))
								wprint(i);
						}
					}
					else{
						for(i=0;i<work.length;i++){
							if(work[i][6].equals("其它"))
								wprint(i);}
					}
				}
				else{
					System.out.println("Input is illegal!\nPlease try again.");
					System.out.println("The Number doesn't exist!");
					System.out.print("Press enter to continue....");
					Scanner input = new Scanner(System.in); 
					input.hasNextLine();
					continue;
				}
				System.out.print("Press enter to continue....");
				Scanner input = new Scanner(System.in); 
				input.hasNextLine();
			}
			
			else if (uc.equalsIgnoreCase("B"))
				break;
			else if (uc.equalsIgnoreCase("E")){
				stAc();
				stWd();
				System.exit(0);
			}
			else{
				System.out.println("Input is illegal!\nPlease try again.");
				System.out.println("The Number doesn't exist!");
				System.out.print("Press enter to continue....");
				Scanner input = new Scanner(System.in); 
				input.hasNextLine();
				continue;
			}
			
			
		}
				
	}//end print
	
	static public void delWork(){
		System.out.println("\n\nWhich work do you want to delete?");
		System.out.print("Please enter its Number:");
		String cgc=jku.next();
			for(i=0;i<work.length;i++){
				if(work[i][5].equalsIgnoreCase(cgc)){
					break;
				}
				else if(i==work.length-1){
					System.out.println("The Number doesn't exist!");
					return;
				}
			}
		String[][] temp=new String[work.length-1][8];
		l=0;
		for(j=0;j<work.length;j++){
			if (i==j)
				continue;
			else{
				for(k=0;k<8;k++)
					temp[l][k]=work[j][k];
			l++;	
			}
		}
			work=temp;
			System.out.println("The work has deleted sucessfully!");
	}//end del
	
	static public void alterWork(){

		System.out.println("\n\nWhich work do you want to alter?");
			System.out.print("Please enter its Number:");
			String cgc=jku.next();
				for(i=0;i<work.length;i++){
					if(work[i][5].equalsIgnoreCase(cgc)){
						break;
					}
					else if(i==work.length-1){
						System.out.println("The Number doesn't exist!");
						return;
					}
				}
				
			for(;;){

			System.out.println("\n\nWhat do you want to alter?");
			System.out.println("Title(T)");
			System.out.println("Finish Day(FD)");
			System.out.println("Start Day(SD)");
			System.out.println("Percentage(PR)");
			System.out.println("Categort(CG)");
			System.out.println("Content(CT)\n");
			System.out.println("\nBack to Work interface(B)");
			System.out.println("Exit System(E)");
			String uc=jku.next();
			int year,month,day,pr,CGcode;
			
			
			if(uc.equalsIgnoreCase("T")){
				System.out.print("Please enter new Title:");
				String nt=jku.next();
				work[i][0]=nt;
			}
			else if(uc.equalsIgnoreCase("FD")){
				System.out.print("Please enter the year first(XXXX):");
				year=jku.nextInt();
					while(year<0||year>9999){
						System.out.print("Input is illegal!\nPlease enter again:");
						year=jku.nextInt();
					}
					System.out.print("Please enter the month next(XX):");
					month=jku.nextInt();
					while(month<0||month>12){
						System.out.print("Input is illegal!\nPlease enter again:");
						month=jku.nextInt();
					}
					System.out.print("Please enter the day last(XX):");
					day=jku.nextInt();
					while(day<0||day>31){
						System.out.print("Input is illegal!\nPlease enter again:");
						day=jku.nextInt();
					}
					work[i][1]=String.format("%04d.%02d.%02d", year,month,day);
					
			}
			else if(uc.equalsIgnoreCase("SD")){
				System.out.print("Please enter the year first(XXXX):");
				year=jku.nextInt();
					while(year<0||year>9999){
						System.out.print("Input is illegal!\nPlease enter again:");
						year=jku.nextInt();
					}
					System.out.print("Please enter the month next(XX):");
					month=jku.nextInt();
					while(month<0||month>12){
						System.out.print("Input is illegal!\nPlease enter again:");
						month=jku.nextInt();
					}
					System.out.print("Please enter the day last(XX):");
					day=jku.nextInt();
					while(day<0||day>31){
						System.out.print("Input is illegal!\nPlease enter again:");
						day=jku.nextInt();
					}
					work[i][2]=String.format("%04d.%02d.%02d", year,month,day);
					
			}
			else if(uc.equalsIgnoreCase("PR")){
				System.out.print("Please enter the NEW percentage of the work(0~100):");
				pr=jku.nextInt();
				while(pr<0||pr>100){
					System.out.print("Input is illegal!\nPlease enter again:");
					day=jku.nextInt();
				}
			if(pr==100)
				work[i][4]="已完成";
			else if(pr<100&&pr>0)
				work[i][4]="執行中";
			else
				work[i][4]="未開始";
			work[i][3]=Integer.toString(pr);
			}
			else if(uc.equalsIgnoreCase("CG")){
				System.out.println("Please enter the NEW category code.");
				System.out.println("日常( 0 )\n娛樂( 1 )\n工作( 2 )\n其它( 3 )");	
				CGcode=jku.nextInt();
				while(CGcode<0||CGcode>3){
					System.out.print("Input is illegal!\nPlease enter again:");
					CGcode=jku.nextInt();
				}
				if(CGcode==0)
					work[i][6]="日常";
					else if(CGcode==1)
						work[i][6]="娛樂";
					else if(CGcode==2)
						work[i][6]="工作";
					else
						work[i][6]="其它";
			}
			else if(uc.equalsIgnoreCase("CT")){
				System.out.println("Please describe the NEW content of the work.");
				System.out.print("(it couldn't include any blank):");
				work[i][7]=jku.next();
			}
			else if(uc.equalsIgnoreCase("B")){
				break;
			}
			else if(uc.equalsIgnoreCase("E")){
				stAc();
				stWd();
				System.exit(0);
			}
			else{
				System.out.println("Input is illegal!\nPlease try again.");
				System.out.println("The Number doesn't exist!");
				System.out.print("Press enter to continue....");
				Scanner input = new Scanner(System.in); 
				input.hasNextLine();
				continue;
			}
		}//end alter interface
			//notice status woule change with PR
	}//end alter
	
	
	
	
	
	
	
	
	
	
	
	
	
	//************************WORKS DATA**************************************
	//************************ACCOUNT DATA**************************************
	static int rn=0;//rows number
	
	static String[] account;//帳號陣列
	static String[] password;//密碼陣列
	static String[] or;//原本每一行的樣子
	
	static void countLine()throws IOException{
	while((ts=br.readLine())!=null) //算出總共幾行
		rn++;
	account=new String[rn];//帳號陣列
	password=new String[rn];//密碼陣列
	or=new String[rn];//原本每一行的樣子
	}
	
	static void setOr()throws IOException{
		for(i=0;i<rn;i++)
			or[i]=br2.readLine();
	}
	
	
	static void setAnP(){
		for(i=0;i<rn;i++){//裝入帳密
			tk= new StringTokenizer(or[i]," ");
			
				account[i]=tk.nextToken();
				password[i]=tk.nextToken();
			
		}//END裝填帳密
	}
	
static boolean checkExistedAccount(String name){
	for(i=0;i<account.length;i++)//檢查帳號是否重複
		if(account[i].equals(name)){
			return false;
		}
	return true;
}



static void addAccount(){//創帳號
	String userName,pass,secp;
	System.out.print("Set the user name:");
	userName=jku.next();
	if( checkExistedAccount(userName) ){
		System.out.print("Set the password:");
		hpa = cnsl.readPassword();
		pass=String.valueOf(hpa);
		System.out.print("Enter the password again:");
		hpa = cnsl.readPassword();
		secp=String.valueOf(hpa);
		while(!pass.equals(secp)){
			System.out.println("The passwords entered are not consistent!");
			System.out.println("Please try again.");
			System.out.print("Set the password:");
			hpa = cnsl.readPassword();
			pass=String.valueOf(hpa);
			System.out.print("Enter the password again:");
			hpa = cnsl.readPassword();
			secp=String.valueOf(hpa);
		}
			account=Arrays.copyOf(account,account.length+1);
			account[account.length-1]=userName;
			password=Arrays.copyOf(password,password.length+1);
			password[password.length-1]=pass;
			System.out.println("The user "+userName+" has created successfully!");
		}
	
	else{
		System.out.println("The user name had existed.Please try again.");
		addAccount();
	}
}//end Add NO PARAMETERS

static void delAccount(){//刪帳號
	String userName,pass;
	System.out.print("Enter the user name:");
	userName=jku.next();
	if(userName.equals("cis")){
		System.out.println("The admin account cannot be deleted!");
		return;
	}
	
	if(!checkExistedAccount(userName)){
	for(i=0;i<account.length;i++)
		if(account[i].equals(userName)){
			System.out.print("Enter the password:");
			hpa = cnsl.readPassword();
			pass=String.valueOf(hpa);
			if(password[i].equals(pass))
				break;
			else{
				System.out.println("Wrong Password!");
				
				return;}
		}
	}
	else{
		System.out.println("The user name doesn't exist!");
		
		
		return;
	}
	String[] ba=account;
	String[] bp=password;
	account=Arrays.copyOf(account,account.length-1);
	for(j=0,k=0;j<ba.length;j++){
		
		if(j==i) continue;
		else{
			account[k]=ba[j];k++;
		}
	}
	password=Arrays.copyOf(password,password.length-1);
	for(j=0,k=0;j<bp.length;j++){
		if(j==i) continue;
		else{
			password[k]=bp[j];k++;
		}
	}
	System.out.println("The user "+userName+" has deleted successfully!");
}//end Del
static boolean checkName(String name){
	for(j=0;j<3;j++){
	for(i=0;i<account.length;i++){
		if(account[i].equals(name)){
			wt=0;
			System.out.print("Enter your Password:");
			hpa = cnsl.readPassword();
			String pass=String.valueOf(hpa);
			return checkPass(pass,i);
		}
	}
		wt++;
		if(wt==3)  
			break;
		System.out.print("The UserName doesn't exist!");
		System.out.printf("You still have %d chances\n",3-wt);
		
		System.out.print("\nPlease enter again:");
		String Rname=jku.next();
		name=Rname;
		}
	return false;
}//end checkName

static boolean checkPass(String pass,int site){
	for(j=0;j<3;j++){
		if(password[site].equals(pass))
			return true;
		
			wt++;
			if(wt==3) break;
			System.out.print("Wrong password!");
			System.out.printf("You still have %d chances\n",3-wt);
			System.out.print("\nPlease enter again:");
			hpa = cnsl.readPassword();
			String Rpass=String.valueOf(hpa);
			pass=Rpass;
		}
	return false;
}

static void stAc(){
	PrintWriter output = null;
	try
    {
        output = new PrintWriter(new FileOutputStream("Account.txt"));
    }
    catch(FileNotFoundException e)
    {
        System.out.println("Problem opening files.");
        System.exit(0);
    }

	for(i=0;i<account.length;i++){
		output.printf("%s %s",account[i],password[i]);
		if(i!=account.length-1)
			output.println();
	}
	output.close( );
}
static void stWd(){
	PrintWriter output2 = null;
	try
    {
        output2 = new PrintWriter(new FileOutputStream("works.txt"));
    }
    catch(FileNotFoundException e)
    {
        System.out.println("Problem opening files.");
        System.exit(0);
    }

	for(i=0;i<work.length;i++){
		output2.printf("%-24s %-12s %-12s %-5s %-7s %-8s %-6s %s",work[i][0],work[i][1],work[i][2],work[i][3],work[i][4],work[i][5],work[i][6],work[i][7]);
		if(i!=work.length-1)
			output2.println();
	}
	output2.close( );
}


public static void main(String[] args)throws IOException{ //主程式
	IOE();
	countLine();
	setOr();
	setAnP();
	
	IOEd();
	countLined();
	setOrd();
	setWork();
	
	for(i=0;i<8;i++)
		dr[i]=true;

for(;;){//初始介面

System.out.print("\n\nHello, user.\nWhat do you want to do?\nLogin(L),Exit(E):");

String uc=jku.next();

if(uc.equalsIgnoreCase("L")){//登入畫面
	System.out.print("Enter your Username:");
	String name=jku.next();
	wt=0;
	if( checkName(name) ){//帳密都對
			for(;;){//登入後畫面
				
				if(name.equalsIgnoreCase("cis"))
					root=true;
				else
					root=false;
				
				if(root){
					System.out.println("\n\nYou are admin.");
					System.out.println("What do you want to do?\n\n");
					System.out.println("Add Account(AA)");
					System.out.println("Delete Account(DA)");
					System.out.println("Enter Works Data interface(WD)");
					
					System.out.println("\nLogout Account(LA)");
					System.out.println("Exit System(E)");
					uc=jku.next();
					if(uc.equalsIgnoreCase("AA")){
						addAccount();
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
					}
					else if(uc.equalsIgnoreCase("DA")){
						delAccount();
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
					}
					else if(uc.equalsIgnoreCase("WD")){
						for(;;){//Work Data Interface
							System.out.println("Now is at Work Interface.");
							if(root)
								System.out.print("Admin, ");
							else
								System.out.print("User, ");
							System.out.print("what do you want to do?\n");
							System.out.println("Create new Work(CW)");
							System.out.println("Search for Work(SW)");
							System.out.println("Print Work(PW)");
							System.out.println("Delete Work(DW)");
							System.out.println("Alter Work(AW)");
							System.out.println("Display rights switch(DS)");
							
							System.out.println("\nBack to Main interface(B)");
							System.out.println("Exit System(E)");
							
							uc=jku.next();
							
							if(uc.equalsIgnoreCase("CW")){
								createWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("SW")){
								searchWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("PW")){
								printWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("DW")){
								delWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("AW")){
								alterWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("DS")){
								drSwitch();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("B"))
								break;
							
							else if(uc.equalsIgnoreCase("E")){
								stAc();
								stWd();
								System.exit(0);
							}
							else {
								System.out.println("Illegal entry! Please enter again.");
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
								continue;
							}
						}//end Work Data Interface
					}//WD END
					else if(uc.equalsIgnoreCase("LA")){
						break;
					}
					else if(uc.equalsIgnoreCase("E")){
						stAc();
						stWd();
						System.exit(0);
					}
					else{
						System.out.println("Illegal Input!!");
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
					}
						

				}
				else{
					System.out.println("You are user.");
					System.out.println("What do you want to do?\n\n");
					System.out.println("Enter Works Data interface(WD)");
					System.out.println("\nLogout Account(LA)");
					System.out.println("Exit System(E)");
					System.out.println("");
					
					uc=jku.next();
					if(uc.equalsIgnoreCase("AA")){
						System.out.println("You are not admin!");
						System.out.print("Press enter to continue....");
						Scanner input = new Scanner(System.in); 
						input.hasNextLine();
					}
					else if(uc.equalsIgnoreCase("DA")){
						System.out.println("You are not admin!");
					System.out.print("Press enter to continue....");
					Scanner input = new Scanner(System.in); 
					input.hasNextLine();}
					else if(uc.equalsIgnoreCase("WD")){
						for(;;){//Work Data Interface
							System.out.println("Now is at Work Interface.");
							if(root)
								System.out.print("Admin, ");
							else
								System.out.print("User, ");
							System.out.print("what do you want to do?\n");
							System.out.println("Create new Work(CW)");
							System.out.println("Search for Work(SW)");
							System.out.println("Print Work(PW)");
							System.out.println("Delete Work(DW)");
							System.out.println("Alter Work(AW)");
							
							
							System.out.println("\nBack to Main interface(B)");
							System.out.println("Exit System(E)");
							
							uc=jku.next();
							
							if(uc.equalsIgnoreCase("CW")){
								createWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("SW")){
								searchWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("PW")){
								printWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("DW")){
								delWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							else if(uc.equalsIgnoreCase("AW")){
								alterWork();
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
							}
							
							else if(uc.equalsIgnoreCase("B"))
								break;
							
							else if(uc.equalsIgnoreCase("E")){
								stAc();
								stWd();
								System.exit(0);
							}
							else {
								System.out.println("Illegal entry! Please enter again.");
								System.out.print("Press enter to continue....");
								Scanner input = new Scanner(System.in); 
								input.hasNextLine();
								continue;
							}
						}//end Work Data Interface
					}//WD END
					else if(uc.equalsIgnoreCase("LA")){
						break;
					}
					else if(uc.equalsIgnoreCase("E")){
						stAc();
						stWd();
						System.exit(0);
					}
					else{
						System.out.println("Illegal Input!!");
					}
					
				}
				
			}//END 登入後畫面
		}
	
	else {System.out.println("輸入錯誤達三次，將跳回初始畫面");
	System.out.print("Press enter to continue....");
	Scanner input = new Scanner(System.in); 
	input.hasNextLine();
	
	continue;}
	



}//end 登入畫面

else if(uc.equalsIgnoreCase("E"))
	break;
else {
	System.out.println("Illegal entry! Please enter again.");
	System.out.print("Press enter to continue....");
	Scanner input = new Scanner(System.in); 
	input.hasNextLine();
	continue;
}




}//初始介面END
stAc();
stWd();



}//end main
}//end class