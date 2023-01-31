/*
Title: Stable Matching
Purpose: Prepare an algorithm for matching stable pairs using data from a file.
Author: Sam McFarland
Date: 1/15/2023
Time Complexity: O(n^2)
*/
import java.util.*;
import java.io.*;

public class StableMatching {
   public static void main(String[] args) {
      // Opens the file and begins scanning the input. Throws a FileNotFoundException if prefs.txt does not exist.
      File input = new File("prefs.txt");
      Scanner in;
      try {
         in = new Scanner(input);
      }
      catch (FileNotFoundException e) {
         System.err.println("FileNotFoundException: " + e.getMessage());
         in = new Scanner(System.in);
      }
      
      // Initializes the types and groups for the two categories, then begins scanning line by line.
      String type1 = "";
      String type2 = "";
      ArrayList<PrefList> group1 = new ArrayList<PrefList>();
      ArrayList<PrefList> group2 = new ArrayList<PrefList>();
      
      while (in.hasNextLine()) {
         String line = in.nextLine();
         Scanner lineScan = new Scanner(line);
         
         // If the line is empty, skips to the next line. Otherwise, the first "word" is the type of this line.
         if (!lineScan.hasNext()) {
            continue;
         }
         String type = lineScan.next();
         // If the line has only one token, skips to the next line.
         if (!lineScan.hasNext()) {
            System.err.println("Line too short. Skipping...");
            continue;
         }
         
         // If there is no type 1, or if type 1 is the same as this line's type, this line's data is read into a new PrefList in group 1's ArrayList.
         if (type1 == "" || type1.equals(type)) {
            type1 = type;
            PrefList prefs = new PrefList(lineScan.next());
            while (lineScan.hasNext()) {
               prefs.addPref(lineScan.next());
            }
            group1.add(prefs);
         }
         // Otherwise, does the same check for type 2 and thereby group 2.
         else if (type2 == "" || type2.equals(type)) {
            type2 = type;
            PrefList prefs = new PrefList(lineScan.next());
            while (lineScan.hasNext()) {
               prefs.addPref(lineScan.next());
            }
            group2.add(prefs);
         }
         // If both group types exist and neither matches this line's type, the line is skipped.
         else {
            System.out.println("Type " + type + " unrecognized. Skipping...");
         }
      }
// //    Uncomment to test--outputs each group of PrefLists at the end of input reading
//       System.out.println("Group 1 (" + type1 + "):");
//       for (int i = 0; i < group1.size(); i++) {
//          System.out.println(group1.get(i).getName());
//          System.out.println(group1.get(i).getList());
//       }
//       System.out.println();
//       System.out.println("Group 2 (" + type2 + "):");
//       for (int i = 0; i < group2.size(); i++) {
//          System.out.println(group2.get(i).getName());
//          System.out.println(group2.get(i).getList());
//       }
     
      // Initializes the HashMap of matches, making sure it has enough capacity for the maximum required number of matches
      HashMap<String, String> matches = new HashMap<String, String>(Math.min(group1.size(), group2.size()));
      
      // Runs the match method on each PrefList in the proposing group. 
      // It doesn't matter here if the receiving group changes loyalties; that's dealt with in the function.
      for (int i = 0; i < group1.size(); i++) {
         PrefList proposer = group1.get(i);
         match(proposer, group2, group1, matches);
      }
      
      System.out.println("Final stable matches:");
      System.out.println(matches);
      
// //    Uncomment to test--outputs each group of PrefLists at the end of matching
//       System.out.println("Group 1 (" + type1 + "):");
//       for (int i = 0; i < group1.size(); i++) {
//          System.out.println(group1.get(i).getName());
//          System.out.println(group1.get(i).getList());
//       }
//       System.out.println();
//       System.out.println("Group 2 (" + type2 + "):");
//       for (int i = 0; i < group2.size(); i++) {
//          System.out.println(group2.get(i).getName());
//          System.out.println(group2.get(i).getList());
//       }
   }
   
   // Matching method. Follows the alternate algorithm Karena presented in class.
   public static void match(PrefList proposer, ArrayList<PrefList> prefs, ArrayList<PrefList> others, HashMap<String, String> matches) {
      String prefName = "";
      // Finds the first name in the proposer's preference list that's also in the list of potential matches. If no potential match is found, the function ends.
      for (int i = 0; i < proposer.getList().size(); i++) {
         if (findName(prefs, proposer.getPref(i)) > -1) {
            prefName = proposer.getPref(i);
            break;
         }
      }
      if (prefName != "") {
         boolean breakup = false;
         String loserName = "";
         
         // Checks whether the top preference is already in a match (which in this algorithm is always with a less preferred competitor). 
         // If so, breaks up that match and sets variables for later to recursively reassign a match to the "loser."
         if (matches.containsKey(prefName)) {
            breakup = true;
            loserName = matches.get(prefName);
            matches.remove(prefName);
         }
         
         // Puts a new match between the proposer and the top preference in the matches HashMap, then removes all lower-preferred proposers from the preference's PrefList.
         matches.put(prefName, proposer.getName());
         PrefList pref = prefs.get(findName(prefs, prefName));
         for (int i = pref.getList().size() - 1; i > pref.indexOf(proposer.getName()); i--) {
            if (findName(others, pref.getPref(i)) == -1) {
               continue;
            }
            PrefList loser = others.get(findName(others, pref.getPref(i)));
            loser.removePref(prefName);
            pref.removePref(pref.getPref(i));
         }
         
         // If earlier operations broke up a match, recursively calls this operation again to find a new match for the broken-off proposer.
         if (breakup) {
            match(others.get(findName(others, loserName)), prefs, others, matches);
         }
      }
      return;
   }
   // Finds a PrefList with the given name in the given ArrayList.
   public static int findName(ArrayList<PrefList> prefs, String name) {
      for (int i = 0; i < prefs.size(); i++) {
         if (prefs.get(i).getName().equals(name)) {
            return i;
         }
      }
      return -1;
   }
}
