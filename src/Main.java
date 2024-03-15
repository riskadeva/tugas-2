import java.util.InputMismatchException;
import java.util.Scanner;

class Student {
    public String name;
    public String faculty;
    public String studyProgram;
    public String borrowedBookID;

    public Student(String name, String faculty, String studyProgram) {
        this.name = name;
        this.faculty = faculty;
        this.studyProgram = studyProgram;
    }

    public void logout() {
        System.out.println("Logging out...");
    }

    public void displayBooks(String[][] bookList) {
        System.out.println("=== Daftar Buku ===");
        System.out.printf("| %-5s | %-20s | %-20s | %-5s |\n", "ID", "Judul", "Penulis", "Stok");
        for (String[] book : bookList) {
            System.out.printf("| %-5s | %-20s | %-20s | %-5s |\n", book[0], book[1], book[2], book[3]);
        }
    }

    public void displayBorrowedBook(String[][] bookList) {
        System.out.println("=== Buku yang Dipinjam ===");
        System.out.printf("| %-5s | %-20s | %-20s |\n", "ID", "Judul", "Penulis");
        for (String[] book : bookList) {
            if (book[0].equals(borrowedBookID)) {
                System.out.printf("| %-5s | %-20s | %-20s |\n", book[0], book[1], book[2]);
                break;
            }
        }
    }
}

class Admin {
    String adminUsername = "riska nurhayati deva";
    String password = "riska123";
    boolean studentAddPermission;

    public Admin(boolean studentAddPermission) {
        this.studentAddPermission = studentAddPermission;
    }


    public void addStudent(String[][] userStudent, String name, String nim, String faculty, String studyProgram) {
        String[][] newUserStudent = new String[userStudent.length + 1][4];
        for (int i = 0; i < userStudent.length; i++) {
            newUserStudent[i] = userStudent[i];
        }
        newUserStudent[userStudent.length] = new String[]{nim, name, faculty, studyProgram};
        userStudent = newUserStudent;

        System.out.println("Mahasiswa berhasil ditambahkan.");
    }

    public void displayStudents(String[][] userStudent) {
        System.out.println("=== Daftar Mahasiswa ===");
        for (String[] student : userStudent) {
            System.out.println("NIM: " + student[0]);
            System.out.println("Nama: " + student[1]);
            System.out.println("Fakultas: " + student[2]);
            System.out.println("Program Studi: " + student[3]);
            System.out.println();
        }
    }
}

public class Main {
    String[][] bookList = {
            {"1234", "Harry Potter", "J.K. Rowling", "5"},
            {"5678", "Lord of the Rings", "J.R.R. Tolkien", "3"}
    };
    String[][] userStudent = {
            {"202310370311371", "Riska Deva", "Teknik", "informatika"}
    };

    Admin admin;
    Student student;
    Scanner scanner = new Scanner(System.in);

    void menu() {
        System.out.println("=== Menu Login ===:");
        System.out.println("1. Masuk sebagai Mahasiswa");
        System.out.println("2. Masuk sebagai Admin");
        System.out.println("3. Keluar");
    }

    void inputNIM() {
        System.out.print("Masukkan NIM Anda: ");
        String inputNIM = scanner.next();
        checkNIM(inputNIM);
    }

    void checkNIM(String nim) {
        boolean found = false;
        for (String[] user : userStudent) {
            if (user[0].equals(nim)) {
                found = true;
                System.out.println("Login Mahasiswa Berhasil. Selamat datang, " + user[1] + "!");
                student = new Student(user[1], user[2], user[3]);
                student.displayBooks(bookList);
                menuStudent();
                return;
            }
        }
        if (!found)
            System.out.println("Mahasiswa Tidak Ditemukan.");
        menu();
    }

    void menuAdmin() {
        System.out.println("=== Dashboard Admin ===");
        System.out.println("1. Tambah Mahasiswa");
        System.out.println("2. Tampilkan Daftar Mahasiswa");
        System.out.println("3. Keluar");
        System.out.print("Pilih opsi (1-3): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                addStudent();
                break;
            case 2:
                admin.displayStudents(userStudent);
                menuAdmin();
                break;
            case 3:
                menu();
                break;
            default:
                System.out.println("Pilihan Tidak Valid.");
        }
    }

    void addStudent() {
        System.out.println("=== Menambahkan Mahasiswa ===");
        System.out.print("Masukkan Nama Mahasiswa: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Masukkan NIM Mahasiswa: ");
        String nim = scanner.next();
        System.out.print("Masukkan Fakultas Mahasiswa: ");
        String faculty = scanner.next();
        System.out.print("Masukkan Program Studi Mahasiswa: ");
        String studyProgram = scanner.next();

        admin.addStudent(userStudent, name, nim, faculty, studyProgram);

        menuAdmin();
    }

    void borrowBook() {
        System.out.println("=== Meminjam Buku ===");
        System.out.print("Masukkan ID Buku yang ingin dipinjam: ");
        String bookID = scanner.next();

        boolean bookFound = false;
        for (String[] book : bookList) {
            if (book[0].equals(bookID)) {
                bookFound = true;
                int stock = Integer.parseInt(book[3]);
                if (stock > 0) {
                    System.out.println("Buku '" + book[1] + "' berhasil dipinjam.");
                    stock--;
                    book[3] = String.valueOf(stock);
                    student.borrowedBookID = bookID;
                } else {
                    System.out.println("Maaf, stok buku tidak mencukupi.");
                }
                break;
            }
        }
        if (!bookFound) {
            System.out.println("Buku dengan ID tersebut tidak ditemukan.");
        }

        menuStudent();
    }

    void returnBook() {
        System.out.println("=== Melihat Buku yang Dipinjam ===");
        student.displayBorrowedBook(bookList);
        menuStudent();
    }

    void menuStudent() {
        System.out.println("=== Dashboard Mahasiswa ===");
        System.out.println("1. Pinjam Buku");
        System.out.println("2. Lihat Buku yang Dipinjam");
        System.out.println("3. Keluar");
        System.out.print("Pilih opsi (1-3): ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                borrowBook();
                break;
            case 2:
                returnBook();
                break;
            case 3:
                student.logout();
                menu();
                break;
            default:
                System.out.println("Pilihan Tidak Valid.");
                menuStudent();
        }
    }


    void logout() {
        System.out.println("Terima kasih telah menggunakan program. Sampai jumpa!");
        scanner.close();
    }

    public static void main(String[] args) {
        Main main = new Main();
        boolean studentAddPermission = true;

        main.admin = new Admin(studentAddPermission);

        main.menu();

        while (true) {
            System.out.print("Pilih opsi (1-3): ");
            try {
                int opsi = main.scanner.nextInt();

                switch (opsi) {
                    case 1:
                        System.out.println("Masuk sebagai Mahasiswa:");
                        main.inputNIM();
                        break;

                    case 2:
                        System.out.println("Masuk sebagai Admin:");
                        System.out.print("Masukkan username: ");
                        String inputUsername = main.scanner.next();
                        System.out.print("Masukkan password: ");
                        String inputPassword = main.scanner.next();

                        if (inputUsername.equals(main.admin.adminUsername) && inputPassword.equals(main.admin.password)) {
                            main.menuAdmin();
                        } else {
                            System.out.println("Admin Tidak Ditemukan.");
                            main.menu();
                        }
                        break;

                    case 3:
                        main.logout();
                        return;

                    default:
                        System.out.println("Pilihan Tidak Valid.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Masukkan harus berupa bilangan bulat.");
                main.scanner.next();
            }
        }
    }
}