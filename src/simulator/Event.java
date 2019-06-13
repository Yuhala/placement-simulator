/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator;
/**
 *
 * @author peterson
 */
public class Event implements Comparable{
    
    public int vmId;
    public int eventType;
    public int timeStamp;
    
    public Event(int id,int type,int time){
    this.vmId=id;
    this.eventType=type;
    this.timeStamp=time;
    
    }
    @Override
    public int compareTo(Object comparesto) {
         int compareTime=((Event)comparesto).timeStamp;
         return (this.timeStamp-compareTime);
    }
    
}
