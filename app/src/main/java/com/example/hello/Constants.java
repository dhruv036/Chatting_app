package com.example.hello;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Constants {



    // For Dashboard
    public static String militohhmm(Long mili) {
//        mili = 1627923085000L;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(mili);
        String d = simpleDateFormat.format(calendar1.getTime());
        String dd = String.valueOf(calendar2.get(Calendar.YEAR));  //take current day YEAR
             dd= dd.substring(2,dd.length());
        int now = Integer.parseInt(dd);
        int pre = Integer.parseInt(d);
//        Log.e("TAG1"," "+now);
//        Log.e("TAG2"," "+pre);
        if (now == pre) { // CHECKING YEAR
            simpleDateFormat = new SimpleDateFormat("MM");
            dd = String.valueOf(calendar2.get(Calendar.MONTH));
            d = simpleDateFormat.format(calendar1.getTime());
            pre = Integer.parseInt(d);
            now = Integer.parseInt(dd);
//            Log.e("TAG1"," "+now);
//            Log.e("TAG2"," "+pre);
            if ((now+1) == pre) { // CHECKING MONTH
                simpleDateFormat = new SimpleDateFormat("dd");
                dd = String.valueOf(calendar2.get(Calendar.DATE));
                d = simpleDateFormat.format(calendar1.getTime());

                pre = Integer.parseInt(d);
                now = Integer.parseInt(dd);
                Log.e("TAG1"," "+now);
                Log.e("TAG2"," "+pre);
                if (now == pre) {  // CHECKING DATE
                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                    return simpleDateFormat.format(calendar1.getTime());
                } else {
                    if (now == (pre + 1)) {
                        return "Yesterday";
                    }else {
                        simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                        return simpleDateFormat.format(calendar1.getTime());
                    }
                }

            } else {
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                return simpleDateFormat.format(calendar1.getTime());
            }
        }else {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            return simpleDateFormat.format(calendar1.getTime());
        }
    }

    // For Last seen
    public static String militotimestap(String mili) {
        //    mili = "1630185325000";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(mili));
        String d = simpleDateFormat.format(calendar1.getTime());
        Date date = new Date(); // now
        String dd = String.valueOf(date.getDate());  //take current day date
        int now = Integer.parseInt(dd);
        int pre = Integer.parseInt(d);
        if (now == pre) {
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            return "last seen today at " + simpleDateFormat.format(calendar1.getTime());
        } else {
            if ((now - 1) == pre) {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                return "last seen yesterday at " + simpleDateFormat.format(calendar1.getTime());
            } else {
                simpleDateFormat = new SimpleDateFormat("w");
                if (calendar1.get(Calendar.WEEK_OF_YEAR) == Integer.parseInt(simpleDateFormat.format(date.getTime()))) {
                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                    String day = new SimpleDateFormat("EEEE").format(calendar1.getTime());
                    Log.e("date", day);
                    return "last seen " + day.substring(0, 3) + " " + simpleDateFormat.format(calendar1.getTime());
                }
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                return "last seen at " + simpleDateFormat.format(calendar1.getTime());
            }
        }
    }

    //For Status
    public static String militotime(Long mili) {
//        mili = 1627836685000L;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(mili);
        int d = Integer.parseInt(simpleDateFormat.format(calendar1.getTime()));
        Date date = new Date(); // now

        if (d == (date.getDate())) {
            simpleDateFormat = new SimpleDateFormat("HH");
            int hour2 = Integer.parseInt(simpleDateFormat.format(calendar1.getTime()));
            if (date.getHours() == hour2) {
                simpleDateFormat = new SimpleDateFormat("mm");
                int min1 = Integer.parseInt(simpleDateFormat.format(calendar1.getTime()));
                if (min1 < date.getMinutes()) {
                    int interval = date.getMinutes() - min1;
                    return interval + " minutes ago";
                } else if (min1 == date.getMinutes()) {
                    return "Just now";
                } else {
                }
            } else {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                return "today,  " + simpleDateFormat.format(calendar1.getTime());

            }
        } else {
            if (d == (date.getDate() - 1)) {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                return "Yesterday,  " + simpleDateFormat.format(calendar1.getTime());
            } else {
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                return simpleDateFormat.format(calendar1.getTime());
            }
        }
        return "";
    }

    // For Chatwindow
    public static String militotimess(String mili) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(mili));
        String d = simpleDateFormat.format(calendar1.getTime());
        Date date = new Date(); // now
        String dd = String.valueOf(date.getDate());  //take current day date
        int now = Integer.parseInt(dd);
        int pre = Integer.parseInt(d);
        if (now == pre) {
            //simpleDateFormat = new SimpleDateFormat("HH:mm");
            Log.i("one", "1");
            return "Today";
        } else {
            if (now > (pre + 1)) {
                simpleDateFormat = new SimpleDateFormat("dd:MM:yy");
                String day = String.valueOf(calendar1.get(Calendar.DAY_OF_WEEK));
                Log.i("one", "2");
                return "" + simpleDateFormat.format(calendar1.getTime());
            } else {
                // simpleDateFormat = new SimpleDateFormat("HH:mm");
                Log.i("one", "3");
                return "Yesterday";
            }
        }
    }

    public static String givedate(String mili) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(mili));
        String d = simpleDateFormat.format(calendar1.getTime());
        String dd = String.valueOf(calendar2.get(Calendar.YEAR));  //take current day YEAR
        dd= dd.substring(2,dd.length());
        int now = Integer.parseInt(dd);
        int pre = Integer.parseInt(d);

        if (now == pre) { // CHECKING YEAR
            simpleDateFormat = new SimpleDateFormat("MM");
            dd = String.valueOf(calendar2.get(Calendar.MONTH));
            d = simpleDateFormat.format(calendar1.getTime());
            pre = Integer.parseInt(d);
            now = Integer.parseInt(dd);

            if ((now+1) == pre) { // CHECKING MONTH
                simpleDateFormat = new SimpleDateFormat("dd");
                dd = String.valueOf(calendar2.get(Calendar.DATE));
                d = simpleDateFormat.format(calendar1.getTime());
                pre = Integer.parseInt(d);
                now = Integer.parseInt(dd);
                Log.e("TAG1"," "+now);
                Log.e("TAG2"," "+pre);
                if (now == pre) {  // CHECKING DATE
                    return "Today";
                } else {
                    if (now == (pre + 1)) {
                        return "Yesterday";
                    }else {
                        simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                        return simpleDateFormat.format(calendar1.getTime());
                    }
                }

            } else {
                simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                return simpleDateFormat.format(calendar1.getTime());
            }
        }else {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            return simpleDateFormat.format(calendar1.getTime());
        }
    }

    public static String checktwodate(String mili, String mili2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(Long.parseLong(mili));
        String m1 = simpleDateFormat.format(calendar1.getTime());
        calendar1.setTimeInMillis(Long.parseLong(mili2));
        String m2 = simpleDateFormat.format(calendar1.getTime());
        if (m1.equals(m2)) {
            simpleDateFormat = new SimpleDateFormat("MM");
            calendar1.setTimeInMillis(Long.parseLong(mili));
            m1 = simpleDateFormat.format(calendar1.getTime());
            calendar1.setTimeInMillis(Long.parseLong(mili2));
            m2 = simpleDateFormat.format(calendar1.getTime());
            if (m1.equals(m2)) {
                simpleDateFormat = new SimpleDateFormat("dd");
                calendar1.setTimeInMillis(Long.parseLong(mili));
                m1 = simpleDateFormat.format(calendar1.getTime());
                calendar1.setTimeInMillis(Long.parseLong(mili2));
                m2 = simpleDateFormat.format(calendar1.getTime());
                if (m1.equals(m2)) {
                    Log.i("three", "1");
                    return "same";
                } else {
                    simpleDateFormat = new SimpleDateFormat("dd:MM:yy");
                    calendar1.setTimeInMillis(Long.parseLong(mili));
                    return "" + simpleDateFormat.format(calendar1.getTime());
                }
            } else {
                simpleDateFormat = new SimpleDateFormat("dd:MM:yy");
                calendar1.setTimeInMillis(Long.parseLong(mili));
                return "" + simpleDateFormat.format(calendar1.getTime());
            }

        } else {
            Log.i("three", "2");
            simpleDateFormat = new SimpleDateFormat("dd:MM:yy");
            calendar1.setTimeInMillis(Long.parseLong(mili));
            return "" + simpleDateFormat.format(calendar1.getTime());
        }
    }




}
