import java.util.ArrayList;
import java.util.Scanner;

public class JermBot {
    public static void main(String[] args) {
        // Load hard disk
        HardDiskManager manager = new HardDiskManager("data/list.txt");
        ArrayList<Task> storage = manager.getTasks();

        System.out.println("I'm JermBot...");
        System.out.println("What you want?");

        int numOfItems = storage.size();
        Scanner sc = new Scanner(System.in);
        String currStr = sc.nextLine();
        while (true) {
            if (currStr.equals("bye")) {
                break;
            }

            if (currStr.equals("list")) {
                for (int i = 0; i < numOfItems; i++) {
                    System.out.print(i + 1);
                    System.out.print(". " + storage.get(i).toString() + "\n");
                }
            } else {
                try {
                    String[] splitStr = currStr.split(" ");

                    if (splitStr.length == 2 && (splitStr[0].equals("mark") || splitStr[0].equals("unmark"))) {
                        try {
                            int itemNumber = Integer.parseInt(splitStr[1]);
                            if (splitStr[0].equals("mark")) {
                                storage.get(itemNumber - 1).markDone();
                                System.out.println("Ok good job lor you finished this task:");
                                System.out.println("   " + storage.get(itemNumber - 1).toString());
                            } else {
                                storage.get(itemNumber - 1).markUndone();
                                System.out.println("Wah why you never do this task:");
                                System.out.println("   " + storage.get(itemNumber - 1).toString());
                            }
                        } catch (NumberFormatException e) {
                            throw new WrongInputException();
                        }
                    } else if (splitStr.length == 2 && splitStr[0].equals("delete")) {
                        try {
                            int itemNumber = Integer.parseInt(splitStr[1]);
                            System.out.println("Ok slacker I've removed this task:");
                            System.out.println("   " + storage.get(itemNumber - 1).toString());
                            storage.remove(itemNumber - 1);
                            System.out.println("Now you have " + storage.size() + " tasks in the list. Happy anot.");
                            numOfItems--;
                        } catch (NumberFormatException e) {
                            throw new WrongInputException();
                        }
                    } else if (splitStr[0].equals("todo") || splitStr[0].equals("deadline") || splitStr[0].equals("event")) {
                        Task addedTask = new Task("");
                        switch (splitStr[0]) {
                            case "todo":
                                String[] todoSplit = currStr.split("todo");
                                if (todoSplit.length <= 1 || todoSplit[1].trim().equals("")) {
                                    throw new EmptyTodoException();
                                }
                                addedTask = new Todo(todoSplit[1].trim());
                                break;
                            case "deadline":
                                String splitStr1 = currStr.split("deadline ")[1];
                                String[] splitStr2 = splitStr1.split(" /by ");
                                addedTask = new Deadline(splitStr2[0], splitStr2[1]);
                                break;
                            case "event":
                                String splitStr3 = currStr.split("event ")[1];
                                String[] splitStr4 = splitStr3.split(" /");
                                addedTask = new Event(splitStr4[0], splitStr4[1].substring(5), splitStr4[2].substring(3));
                                break;
                        }
                        storage.add(addedTask);
                        numOfItems++;
                        System.out.printf("Haha now you have this task to do:\n   %s\nNow you have %d things to do.\n", addedTask, numOfItems);
                    } else {
                        throw new WrongInputException();
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            currStr = sc.nextLine();
        }

        System.out.println("Good riddance.");
        sc.close();

        // Update hard disk
        manager.updateTasks(storage);
    }
}
