package com.company;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        printSearchResults(1, "1", "1");

	    Scanner stdIn = new Scanner(System.in);
	    String command ="";
	    printWelcome();
        while(!command.equals("quit")) {

            command = stdIn.next();
            switch (command) {
                case "1":
                    processSearch();
                    break;
                case "2":
                    printSearchableFields();
                    break;
            }
        }
    }

    private static void printWelcome() {
        System.out.println("Type 'quit' to exit at any time, Press 'Enter' to continue");
        System.out.println();
        System.out.println();
        System.out.println("\tSelect search options:");
        System.out.println("\t* Press 1 to search Zendesk");
        System.out.println("\t* Press 2 to view a list of searchable fields");
        System.out.println("\t* Type 'quit' to exit");
    }

    private static void processSearch() {
        Scanner stdIn = new Scanner(System.in);

        System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
        int searchNum = stdIn.nextInt();

        System.out.println("Enter search term");
        String searchTerm = stdIn.nextLine();

        System.out.println("Enter search term");
        String searchValue = stdIn.nextLine();

        printSearchResults(searchNum, searchTerm, searchValue);
    }

    private static void printSearchResults(int searchNum, String searchTerm, String searchValue) {
            String fileName = "docs/organizations.json";
            JSONObject jsonObject = parseJsonFile(fileName);
    }

    private static JSONObject parseJsonFile(String fileName) {
        try {
            File f = new File(fileName);
            if(f.exists()) {
                InputStream is = new FileInputStream(fileName);
                String jsonText = IOUtils.toString(is, "UTF-8");
                System.out.println(jsonText);
                JSONArray jsonArr = new JSONArray(jsonText);
                JSONObject json = jsonArr.getJSONObject(0);

                String a = json.get("domain_names").toString();
                System.out.println(a);

                return json;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static void printSearchableFields() {
    }
}
