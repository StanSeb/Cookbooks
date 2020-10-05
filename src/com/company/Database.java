package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Database {

    ResultSet resultSet;
    PreparedStatement stmt;
    Connection conn = null;
    Scanner scan = new Scanner(System.in);

    Database() {

        connectToDatabase();

        showMenu();


    }

    private void showAllCookBooks(){

        System.out.println("Write the name of one of these recipes.");
        System.out.println("Pasta Carbonara\nKorv med mos\nPotatisgratäng\nEgenbakat bröd\nFetaostsallad\nKanelbullar");

        String skipper = scan.nextLine();
        String userInput = scan.nextLine();

        try{
            stmt = conn.prepareStatement("SELECT recipes.name, books.name FROM recipelist" +
                            "\nINNER JOIN recipes ON recipes.id = recipelist.recipesId" +
                            "\nINNER JOIN books ON books.id = recipelist.booksId " +
                            "\nWHERE recipes.name = ?");
            stmt.setString(1,userInput);
            resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    String row = ("\nRecipe name: " + resultSet.getString(1) + "\nCook book: "
                            + resultSet.getString(2) + "\n-----------------------------");
                    System.out.println(row);
                }
            

        }catch(Exception e){e.printStackTrace();}

    }

    private void showAllRecipes(){

        try{
            stmt = conn.prepareStatement("SELECT recipes.name, recipes.portions, books.name, books.year, books.cook FROM recipelist\n" +
                    " INNER JOIN recipes ON recipes.id = recipelist.recipesId\n" +
                    " INNER JOIN books ON books.id = recipelist.booksId");
            resultSet = stmt.executeQuery();

            while(resultSet.next()){
                String row = ("Recipe name: " + resultSet.getString(1) + " Recipe portions: " + resultSet.getString(2)
                + " Cook book: " + resultSet.getString(3) + " Year: " + resultSet.getString(4)
                + " Cook: " + resultSet.getString(5) +
                        "\n---------------------------------------------------------" +
                        "-----------------------------------------------------------");
                System.out.println(row);
            }
        }catch(Exception e){e.printStackTrace();}

    }

    private void openCookBook() {
        System.out.println("Which cook book do you want to open?");
        System.out.println("1: Lindas-kokbok\n2: Pelles Skafferi\n3: Åbingers bullar");
        int userInput = scan.nextInt();

        try {
            stmt = conn.prepareStatement("SELECT recipes.name, recipes.time, recipes.portions, books.name FROM recipelist\n" +
                    "INNER JOIN recipes ON recipes.id = recipelist.recipesId\n" +
                    "INNER JOIN books ON books.id = recipelist.booksId\n" +
                    "WHERE books.id = ?");
            stmt.setInt(1,userInput);
            resultSet = stmt.executeQuery();

            while(resultSet.next()){
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2)
                + " " + resultSet.getString(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showMenu() {
        System.out.println("What do you want to do?");
        System.out.println("1: Show all recipes." +
                "\n2: List from a specific cook book." +
                "\n3: List cook books having a specific recipe." +
                "\n4: Exit program.");

        int userInput = scan.nextInt();

        switch (userInput) {

            case 1:
                showAllRecipes();
                break;
            case 2:
                openCookBook();
                break;
            case 3:
                showAllCookBooks();
                break;
            case 4:
                System.exit(0);

        }
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:COOKBOOK-DATABASE-DB.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}