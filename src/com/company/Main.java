package com.company;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final String[] userTermArr = {"_id", "url", "external_id", "name", "alias", "created_at", "active", "verified", "shared", "locale", "timezone", "last_login_at", "email", "phone", "signature", "organization_id", "tags", "suspended", "role"};
    private static final String[] ticketTermArr = {"_id", "url", "external_id", "created_at", "type", "subject", "description", "priority", "status", "submitter_id", "assignee_id", "organization_id", "tags", "has_incidents", "due_at", "via"};
    private static final String[] organizationTermArr = {"_id", "url", "external_id", "name", "domain_names", "created_at", "details", "shared_tickets", "tags"};
    private static final String lineDivider = "----------------------------";

    public static void main(String[] args) {
	    Scanner stdIn = new Scanner(System.in);
	    String command ="";
        while(!command.equals("quit")) {
            printWelcome();
            command = stdIn.next();
            switch (command) {
                case "1":
                    processSearch();
                    break;
                case "2":
                    printValidSearchTerms();
                    break;
            }
        }
    }
    public static void printWelcome() {
        System.out.println("Type 'quit' to exit at any time, Press 'Enter' to continue");
        System.out.println();
        System.out.println();
        System.out.println("\tSelect search options:");
        System.out.println("\t* Press 1 to search Zendesk");
        System.out.println("\t* Press 2 to view a list of searchable fields");
        System.out.println("\t* Type 'quit' to exit");
    }
    public static void printValidSearchTerms() {
        System.out.println(lineDivider);
        System.out.println("Search Users with");
        for(int i=0; i<userTermArr.length; i++) {
            System.out.println(userTermArr[i]);
        }
        System.out.println(lineDivider);
        System.out.println("Search Tickets with");
        for(int i=0; i<ticketTermArr.length; i++) {
            System.out.println(ticketTermArr[i]);

        }
        System.out.println(lineDivider);
        System.out.println("Search Organizations with");
        for(int i=0; i<organizationTermArr.length; i++) {
            System.out.println(organizationTermArr[i]);
        }
        System.out.println(lineDivider);

    }
    public static void processSearch() {
        Scanner stdIn = new Scanner(System.in);

        System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
        String searchNum = stdIn.nextLine();
        if(searchNum.equals("quit")) {
            System.exit(0);
        }
        boolean searchTermIsValid = false;
        String searchTerm = "";
        while(!searchTermIsValid) {
            System.out.println("Enter search term");
            searchTerm = stdIn.nextLine();

            if(searchTerm.equals("quit")) {
                System.exit(0);
            } else if(searchNum.equals("1") && !Arrays.asList(userTermArr).contains(searchTerm)
                    ||searchNum.equals("2") && !Arrays.asList(ticketTermArr).contains(searchTerm)
                    ||searchNum.equals("3") && !Arrays.asList(userTermArr).contains(searchTerm)) {
                System.out.println("Invalid search term");
            } else {
                searchTermIsValid = true;
            }
        }

        System.out.println("Enter search value");
        String searchValue = stdIn.nextLine();
        if(searchValue.equals("quit")) {
            System.exit(0);
        }
        printSearchResults(searchNum, searchTerm, searchValue);
    }
    public static void printSearchResults(String searchNum, String searchTerm, String searchValue) {
        String fileName = "";
        switch (searchNum) {
                case "1":
                    fileName = "docs/users.json";
                    break;
                case "2":
                    fileName = "docs/tickets.json";
                    break;
                case "3":
                    fileName = "docs/organizations.json";
                    break;
            }

            JSONObject jsonObject = searchJsonFile(fileName, searchNum, searchTerm, searchValue);
    }
    public static JSONObject searchJsonFile(String fileName, String searchNum, String searchTerm, String searchValue) {
        try {
            File f = new File(fileName);
            if(f.exists()) {
                InputStream is = new FileInputStream(fileName);
                String jsonText = IOUtils.toString(is, "UTF-8");
//                System.out.println(jsonText);
                JSONArray jsonArr = new JSONArray(jsonText);
                JSONObject json = new JSONObject();
                boolean foundMatch = false;
                for(int i=0; i< jsonArr.length(); i++) {
                    json = jsonArr.getJSONObject(i);
                    if(json.has(searchTerm) && searchValue.equals(json.get(searchTerm).toString())) {
                        foundMatch = true;
                    }
                }
                if(!foundMatch) {
                    System.out.println("No results found");
                    return null;
                }
                String[] fieldNames = new String[0];
                String[] tickets = new String[0];
                String organization = "";
                String userId = "";
                String orgId = "";
                String submitterId = "";
                String assigneeId = "";
                String submitter = "";
                String assignee = "";

                switch (searchNum) {
                    case "1":
                        userId = Integer.toString(json.getInt("_id"));
                        orgId = Integer.toString(json.getInt("organization_id"));
                        fieldNames = userTermArr;
                        organization = getOrganizationNameById(orgId);
                        tickets = getTicketsBySubmitterId(userId);
                        System.out.println(organization);
                        System.out.println(Arrays.toString(tickets));
                        break;
                    case "2":
                        submitterId = Integer.toString(json.getInt("submitter_id"));
                        assigneeId = Integer.toString(json.getInt("assignee_id"));
                        orgId = Integer.toString(json.getInt("organization_id"));
                        fieldNames = ticketTermArr;
                        organization = getOrganizationNameById(orgId);
                        submitter = getUserNameById(submitterId);
                        assignee = getUserNameById(assigneeId);

                        break;
                    case "3":
                        fieldNames = organizationTermArr;
                        break;
                }
                for(int i=0; i<fieldNames.length; i++) {
                    if(json.has(fieldNames[i].toString())) {
                        System.out.println(fieldNames[i]+": "+json.get(fieldNames[i].toString()));
                    }
                }
                //Print Org Name
                if(!organization.equals("")) {
                    System.out.println("organization_name: "+organization);
                }
                if(!submitter.equals("")) {
                    System.out.println("submitter_name: "+submitter);
                }
                if(!assignee.equals("")) {
                    System.out.println("assignee_name: "+assignee);
                }
                //Print Tickets
                for(int i = 0; i<tickets.length; i++) {
                    System.out.println("ticket_"+i+": "+tickets[i]);
                }
                return json;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String getOrganizationNameById(String orgId) {
        String organizationFileName = "docs/organizations.json";
        try {
            File f = new File(organizationFileName);
            if(f.exists()) {
                InputStream is = new FileInputStream(organizationFileName);
                String jsonText = IOUtils.toString(is, "UTF-8");
                JSONArray jsonArr = new JSONArray(jsonText);

                JSONObject json;
                for(int i=0; i< jsonArr.length(); i++) {
                    json = jsonArr.getJSONObject(i);
                    if(json.get("_id").toString().equals(orgId)) {
                        return json.get("name").toString();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getUserNameById(String userId) {
        String organizationFileName = "docs/users.json";
        try {
            File f = new File(organizationFileName);
            if(f.exists()) {
                InputStream is = new FileInputStream(organizationFileName);
                String jsonText = IOUtils.toString(is, "UTF-8");
                JSONArray jsonArr = new JSONArray(jsonText);

                JSONObject json;
                for(int i=0; i< jsonArr.length(); i++) {
                    json = jsonArr.getJSONObject(i);
                    if(json.get("_id").toString().equals(userId)) {
                        return json.get("name").toString();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String[] getTicketsBySubmitterId(String userId) {

        String ticketFileName = "docs/tickets.json";
        ArrayList<String> tickets = new ArrayList<>();
        try {
            File f = new File(ticketFileName);
            if(f.exists()) {
                InputStream is = new FileInputStream(ticketFileName);
                String jsonText = IOUtils.toString(is, "UTF-8");
                JSONArray jsonArr = new JSONArray(jsonText);

                JSONObject json = new JSONObject();
                for(int i=0; i< jsonArr.length(); i++) {
                    json = jsonArr.getJSONObject(i);
                    if(json.get("submitter_id").toString().equals(userId)) {
                        tickets.add(json.get("subject").toString());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] ticketsArr = tickets.toArray(new String[0]);

        return ticketsArr;
    }
}
