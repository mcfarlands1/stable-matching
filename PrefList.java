import java.util.*;
// PrefList object. Has a String name and an ArrayList of names in order of preference, and basic operations for returning the name and manipulating 
// the list of preferences. Will not accept duplicate preferences, prioritizing highest placement if multiple of one name are found.
public class PrefList {
   private String name;
   private ArrayList<String> prefs;
   
   public PrefList(String name) {
      this.name = name;
      prefs = new ArrayList<String>();
   }
   
   public String getName() {
      return name;
   }
   
   public ArrayList<String> getList() {
      return prefs;
   }
   
   public String getPref(int index) {
      return prefs.get(index);
   }
   
   public int indexOf(String pref) {
      return prefs.indexOf(pref);
   }
   
   public void addPref(String pref) {
      if (prefs.contains(pref)) {
         System.out.println(name + " already contains preference " + pref + ". Skipping duplicate input.");
         return;
      }
      prefs.add(pref);
      return;
   }
   
   public void removePref(String pref) {
      prefs.remove(pref);
   }
}