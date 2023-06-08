import java.util.*;
import java.lang.*;
import java.io.*;

// 불침번 프로그램 (베타1 완성버전)
class Auto_EmployeeList {
    public static void main (String[] args) throws java.lang.Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine(), " ");
        
        int Day=Integer.parseInt(st.nextToken()); //근무기간 (불침번 일수) ex) 3
        int Num_Soldier=Integer.parseInt(st.nextToken()); //총 인원 ex) 17
        int start_Soldier=Integer.parseInt(st.nextToken()); //이번 회차 시작 근무자
        Soldier[] s=new Soldier[Num_Soldier];
        
        for(int i=0; i<Num_Soldier; i++){
            //각 인원의 정보 기입
            st=new StringTokenizer(br.readLine(), " ");
            String name=st.nextToken(); //이름
            int bad_time=Integer.parseInt(st.nextToken()); //기피하는 시간대 들어간 횟수
            int out_start=Integer.parseInt(st.nextToken()); //출타 (휴가, 외박) 시작일
            int out_end=Integer.parseInt(st.nextToken()); //출타 (휴가, 외박) 종료일
            s[i] = new Soldier(name, bad_time, out_start, out_end);
        }
        
        int current_order=start_Soldier-1; //현재 순서
        for(int d=0; d<Day; d++){
            //각 일자의 근무 자동 생성 및 출력
            System.out.println("     < "+(d+1)+"일자 불침번 >");
            //for(int k=0; k<Num_Soldier; k++){
            //    System.out.println(s[k].Name + ": "+s[k].Bad_time);
            //}
            //각 일자의 개수파악을 위한 반복문
            current_order=work_auto(current_order, s, Num_Soldier, d+1);
            System.out.print("\n");
        }

        System.out.println("[다음 회차 첫번째 근무자입니다. 다음번 근무때 적용해주십쇼.]");
        System.out.println(current_order+1 + "(" + s[current_order].Name + ")" );
        System.out.println();

        System.out.println("[말전(03:00~04:30) 근무 현황입니다. 다음번 근무때 적용해주십쇼.]");
        for(int k=0; k<Num_Soldier; k++){
            System.out.println(s[k].Name + ": "+s[k].Bad_time);
        }
        
    }
    
    public static int work_auto(int cs, Soldier[] s, int ns, int day){
        //근무 자동 알고리즘
        //계급순 정렬 -> 출타 적용 -> 기피시간대 적용 순으로 구현예정
        String time="";
        
        if(cs==ns){
            //한바퀴 순환했을 시, 다시 처음부터
           cs=0;
        }
        int i=cs; //근무 순번
        
        int Bad_check=0; //기피 시간대 찾기 위한 인덱스
        //int Bad_check=cs; //전날에 기피 근무를 했는데 다음날 또 하게되는 불상사가 생기는 문제발생.  
        int Bad_first=0; //기피 시간대 3층 근무자
        int Bad_second=0; //기피 시간대 4층 근무자
        
        //기피 시간대의 인원을 먼저 배정하여 구현
        
        //휴가자는 제외하는거 추가해야됨. -> 구현완료
        //다들 1됐을 경우 어떻게 할지 추가해야됨. -> 구현완료
        //출타자인 경우 0에서 올라가지 않아 다들 1이 안될수도. ->이건 감안해야될듯.
        
        //1번 근무자 배정
        int Bad_first_count=0; //한바퀴 돌았는지 체크를 위해
        while(s[Bad_check].Bad_time!=0 || (day>=s[Bad_check].Out_start && day<=s[Bad_check].Out_end)){
            //기피 시간대 근무 횟수가 0이면서 출타자가 아닌 경우 반복문 스탑 (즉, 0이 아니거나 출타자인 경우 반복)
            Bad_check++;
            if(Bad_check==ns){
                //한바퀴 순환했을 시, 다시 처음부터
                Bad_check=0;
            }
            Bad_first_count++;
            if(Bad_first_count==ns){
                //만약 반복문이 한바퀴 돌았을 경우. (기피 시간대 근무 적합자가 존재하지 않는 경우)
                for(int k=0; k<ns; k++){
                    s[k].Bad_time=0;
                }
                //System.out.println("초기화1");
            }
        }
        s[Bad_check].Bad_time++;
        Bad_first=Bad_check; //기피 시간대 첫번째 근무자 배정완료
        
        
        //2번 근무자 배정
        Bad_check++;
        if(Bad_check==ns){
                //한바퀴 순환했을 시, 다시 처음부터
                Bad_check=0;
        }
        int Bad_second_count=0;
        while(s[Bad_check].Bad_time!=0 || (day>=s[Bad_check].Out_start && day<=s[Bad_check].Out_end)){
            //기피 시간대 근무 횟수가 0이면서 출타자가 아닌 경우 반복문 스탑 (즉, 0이 아니거나 출타자인 경우 반복)
            Bad_check++;
            if(Bad_check==ns){
                //한바퀴 순환했을 시, 다시 처음부터
                Bad_check=0;
            }
            Bad_second_count++;
            if(Bad_second_count==ns){
                //만약 반복문이 한바퀴 돌았을 경우. (기피 시간대 근무 적합자가 존재하지 않는 경우)
                for(int k=0; k<ns; k++){
                    s[k].Bad_time=0;
                }
                //System.out.println("초기화2");
            }
        }
        s[Bad_check].Bad_time++;
        Bad_second=Bad_check; //기피 시간대 두번째 근무자 배정완료
        
        
        //근무자 배정 및 근무자표 출력
        for(int k=0; k<5; k++){
            if(k==0){
                time="22:00~24:00";
            }else if(k==1){
                time="24:00~01:30";
            }else if(k==2){
                time="01:30~03:00";
            }else if(k==3){
                time="03:00~04:30";
            }else if(k==4){
                time="04:30~06:30";
            }
            
            //3층
            if(i==ns){
                //한바퀴 순환했을 시, 다시 처음부터
                i=0;
            }
            if(k!=3){
                if(i==Bad_first || i==Bad_second){
                    //기피 시간대 배정된 인원일 경우 순번 넘기기
                    i++;
                    if(i==ns){
                        i=0;
                    }
                    if(i==Bad_first || i==Bad_second){
                        //다음 사람이 또 기피 시간대 인원일 수도 있기에
                        i++;
                        if(i==ns){
                            i=0;
                        }
                    }
                }
                while(day>=s[i].Out_start && day<=s[i].Out_end ){
                    //해당 인원이 출타일 경우 순번 넘기기
                    i++;
                    if(i==ns){
                        i=0;
                    }
                    if(i==Bad_first || i==Bad_second){
                        //기피 시간대 배정된 인원일 경우 순번 넘기기
                        i++;
                        if(i==ns){
                            i=0;
                        }
                        if(i==Bad_first || i==Bad_second){
                            //다음 사람이 또 기피 시간대 인원일 수도 있기에
                            i++;
                            if(i==ns){
                                i=0;
                            }
                        }
                    }
                }
                System.out.print(time + " " + s[i].Name + " ");
                i++;
            }else{
                //기피 시간대 일 경우
                System.out.print(time + " " + s[Bad_first].Name + " ");
            }
            
            
            //4층
            if(i==ns){
                //한바퀴 순환했을 시, 다시 처음부터
                i=0;
            }
            if(k!=3){
                if(i==Bad_first || i==Bad_second){
                    //기피 시간대 배정된 인원일 경우 순번 넘기기
                    i++;
                    if(i==ns){
                        i=0;
                    }
                    if(i==Bad_first || i==Bad_second){
                        //다음 사람이 또 기피 시간대 인원일 수도 있기에
                        i++;
                        if(i==ns){
                            i=0;
                        }
                    }
                }
                while(day>=s[i].Out_start && day<=s[i].Out_end ){
                    //해당 인원이 출타일 경우 순번 넘기기
                    i++;
                    if(i==ns){
                        i=0;
                    }
                    if(i==Bad_first || i==Bad_second){
                        //기피 시간대 배정된 인원일 경우 순번 넘기기
                        i++;
                        if(i==ns){
                            i=0;
                        }
                        if(i==Bad_first || i==Bad_second){
                            //다음 사람이 또 기피 시간대 인원일 수도 있기에
                            i++;
                            if(i==ns){
                                i=0;
                            }
                        }
                    }
                }
                System.out.println(s[i].Name);
                i++;
            }else{
                //기피 시간대 일 경우
                 System.out.println(s[Bad_second].Name);
            }
        }
        
      
        return i;
        
        
    }
}

class Soldier{
    //인스턴스 변수
    String Name; //이름
    int Bad_time; //기피하는 시간대 들어간 횟수
    int Out_start; //출타 (휴가, 외박) 시작일
    int Out_end; //출타 (휴가, 외박) 종료일
   
    //생성자
    Soldier(String n, int bt, int os, int oe){
        this.Name=n;
        this.Bad_time=bt;
        this.Out_start=os;
        this.Out_end=oe;
    }
   
   
}
