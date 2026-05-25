import java.util.ArrayList;
import java.util.Scanner;

// Class untuk merepresentasikan Item Menu (Mengimplementasikan konsep Class & Object)
class MenuItem {
    private String name;
    private double price;
    private String category; // "Makanan" atau "Minuman"

    // Constructor
    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getter dan Setter (Menggunakan tipe data, variabel, identifier)
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
}

// Class untuk merepresentasikan Item yang Dipesan oleh Pelanggan
class OrderItem {
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return menuItem.getPrice() * quantity; }
}

public class RestoranApp {
    // Menggunakan ArrayList (Dynamic Array) agar manajemen menu fleksibel & tidak terbatas
    private static ArrayList<MenuItem> menuList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Inisialisasi data menu awal (Fitur 1: Input Menu Restoran)
        initMenu();

        boolean running = true;
        // Struktur perulangan while untuk Menu Utama Aplikasi
        while (running) {
            System.out.println("\n========================================");
            System.out.println("      APLIKASI MANAJEMEN RESTORAN       ");
            System.out.println("========================================");
            System.out.println("1. Menu Pelanggan (Pemesanan)");
            System.out.println("2. Menu Pemilik (Kelola Restoran)");
            System.out.println("3. Keluar Aplikasi");
            System.out.print("Pilih opsi (1-3): ");
            
            String choice = scanner.nextLine();
            
            // Struktur keputusan switch-case untuk Menu Utama
            switch (choice) {
                case "1":
                    customerMenu();
                    break;
                case "2":
                    ownerMenu();
                    break;
                case "3":
                    running = false;
                    System.out.println("Terima kasih telah menggunakan aplikasi ini!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Silakan masukkan angka 1-3.");
            }
        }
    }

    // Mengisi data awal ke dalam daftar menu restoran
    private static void initMenu() {
        menuList.add(new MenuItem("Nasi Goreng Spesial", 25000, "Makanan"));
        menuList.add(new MenuItem("Ayam Bakar Taliwang", 35000, "Makanan"));
        menuList.add(new MenuItem("Mie Goreng Seafood", 28000, "Makanan"));
        menuList.add(new MenuItem("Es Teh Manis", 6000, "Minuman"));
        menuList.add(new MenuItem("Jus Alpukat Kocok", 15000, "Minuman"));
        menuList.add(new MenuItem("Es Jeruk Peras", 8000, "Minuman"));
    }

    // Menampilkan daftar menu yang tersedia (Menggunakan perulangan for-each)
    private static void displayMenu() {
        System.out.println("\n--- DAFTAR MENU RESTORAN ---");
        System.out.printf("%-4s | %-25s | %-10s | %-10s\n", "No", "Nama Menu", "Kategori", "Harga");
        System.out.println("------------------------------------------------------------");
        int index = 1;
        for (MenuItem item : menuList) {
            System.out.printf("%-4d | %-25s | %-10s | Rp %,10.0f\n", index++, item.getName(), item.getCategory(), item.getPrice());
        }
        System.out.println("------------------------------------------------------------");
    }

    // Fitur 2: Alur Pemesanan Pelanggan (Looping tak terbatas hingga kata 'selesai')
    private static void customerMenu() {
        ArrayList<OrderItem> currentOrders = new ArrayList<>();
        boolean ordering = true;

        while (ordering) {
            displayMenu();
            System.out.println("Ketik 'selesai' jika sudah selesai memilih pesanan.");
            System.out.print("Masukkan Nama Menu yang ingin dipesan: ");
            String inputMenu = scanner.nextLine();

            // Struktur keputusan untuk berhenti memesan (Menggunakan String method equalsIgnoreCase)
            if (inputMenu.equalsIgnoreCase("selesai")) {
                if (currentOrders.isEmpty()) {
                    System.out.println("Keranjang kosong. Kembali ke Menu Utama.");
                    return;
                }
                ordering = false;
                break;
            }

            // Validasi input menu: Mencari apakah menu terdaftar di sistem
            MenuItem selectedItem = null;
            for (MenuItem item : menuList) {
                if (item.getName().equalsIgnoreCase(inputMenu)) {
                    selectedItem = item;
                    break;
                }
            }

            // Jika input teks di luar dari pilihan menu, sistem akan meminta input kembali (continue)
            if (selectedItem == null) {
                System.out.println("Menu tidak ditemukan! Silakan input nama menu dengan benar sesuai daftar.");
                continue;
            }

            // Input kuantitas pesanan dengan penanganan error input non-angka
            int quantity = 0;
            while (true) {
                System.out.print("Masukkan Jumlah Pesanan untuk " + selectedItem.getName() + ": ");
                try {
                    quantity = Integer.parseInt(scanner.nextLine());
                    if (quantity > 0) break;
                    System.out.println("Jumlah pesanan minimal berangka 1!");
                } catch (NumberFormatException e) {
                    System.out.println("Input salah! Kuantitas harus berupa angka.");
                }
            }

            // Jika item yang sama diinput ulang, otomatis lakukan akumulasi jumlah pesanan
            boolean dynamicUpdate = false;
            for (OrderItem order : currentOrders) {
                if (order.getMenuItem().getName().equalsIgnoreCase(selectedItem.getName())) {
                    order.setQuantity(order.getQuantity() + quantity);
                    dynamicUpdate = true;
                    break;
                }
            }

            if (!dynamicUpdate) {
                currentOrders.add(new OrderItem(selectedItem, quantity));
            }
            System.out.println("Berhasil menyimpan " + quantity + " " + selectedItem.getName() + " ke keranjang belanja.");
        }

        // Lanjut ke Fitur 3 & 4: Hitung Biaya dan Cetak Struk
        printReceipt(currentOrders);
    }

    // Fitur 3 & 4: Menghitung Total Biaya & Mencetak Struk Pesanan Lengkap dengan Aturan Diskon
    private static void printReceipt(ArrayList<OrderItem> orders) {
        double rawSubtotal = 0;
        int totalMinumanOrdered = 0;
        OrderItem cheapestDrinkOrder = null;

        // Hitung total kotor dan cari item minuman termurah untuk keperluan promo
        for (OrderItem order : orders) {
            rawSubtotal += order.getTotalPrice();
            if (order.getMenuItem().getCategory().equalsIgnoreCase("Minuman")) {
                totalMinumanOrdered += order.getQuantity();
                if (cheapestDrinkOrder == null || order.getMenuItem().getPrice() < cheapestDrinkOrder.getMenuItem().getPrice()) {
                    cheapestDrinkOrder = order;
                }
            }
        }

        // Ketentuan 3.b: Penawaran beli satu gratis satu jika subtotal > Rp 50.000 dan beli minimal 2 minuman
        double promoDrinkDiscount = 0;
        boolean getPromoDrink = false;
        if (rawSubtotal > 50000 && totalMinumanOrdered >= 2 && cheapestDrinkOrder != null) {
            promoDrinkDiscount = cheapestDrinkOrder.getMenuItem().getPrice();
            getPromoDrink = true;
        }

        // Ketentuan 3.a: Diskon 10% jika total biaya keseluruhan pesanan melebihi Rp 100.000
        double generalDiscount = 0;
        if (rawSubtotal > 100000) {
            generalDiscount = rawSubtotal * 0.10;
        }

        double totalBiayaSetelahDiskon = rawSubtotal - generalDiscount - promoDrinkDiscount;
        
        // Penambahan Pajak 10% dan Biaya Pelayanan tetap Rp 20.000
        double tax = totalBiayaSetelahDiskon * 0.10;
        double serviceCharge = 20000;
        double finalGrandTotal = totalBiayaSetelahDiskon + tax + serviceCharge;

        // Proses Cetak Struk ke Layar Monitor
        System.out.println("\n============================================================");
        System.out.println("                     STRUK NOTA BELANJA                     ");
        System.out.println("============================================================");
        System.out.printf("%-25s %-5s %-12s %-12s\n", "Item Menu", "Qty", "Harga Satuan", "Total Harga");
        System.out.println("------------------------------------------------------------");
        
        for (OrderItem order : orders) {
            System.out.printf("%-25s %-5d Rp %,10.0f Rp %,10.0f\n", 
                order.getMenuItem().getName(), 
                order.getQuantity(), 
                order.getMenuItem().getPrice(), 
                order.getTotalPrice());
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-44s : Rp %,10.0f\n", "Subtotal Pesanan", rawSubtotal);
        
        if (generalDiscount > 0) {
            System.out.printf("%-44s :-Rp %,10.0f\n", "Diskon Member 10% (>100k)", generalDiscount);
        }
        if (getPromoDrink) {
            System.out.printf("%-44s :-Rp %,10.0f\n", "Promo Beli 1 Gratis 1 Minuman", promoDrinkDiscount);
        }
        
        System.out.printf("%-44s : Rp %,10.0f\n", "Total Setelah Potongan", totalBiayaSetelahDiskon);
        System.out.printf("%-44s : Rp %,10.0f\n", "Pajak Restoran (10%)", tax);
        System.out.printf("%-44s : Rp %,10.0f\n", "Biaya Pelayanan (Service)", serviceCharge);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-44s : Rp %,10.0f\n", "TOTAL GRAND TOTAL AKHIR", finalGrandTotal);
        System.out.println("============================================================");
        System.out.println("          Terima Kasih & Selamat Menikmati Hidangan         ");
        System.out.println("============================================================");
        
        System.out.print("\nTekan ENTER untuk kembali ke Menu Utama...");
        scanner.nextLine();
    }

    // Fitur 5: Manajemen Menu Aplikasi (Menu Pemilik Restoran / Parent-Child Navigation)
    private static void ownerMenu() {
        boolean inOwnerMenu = true;
        while (inOwnerMenu) {
            System.out.println("\n--- PANEL PENGELOLAAN MENU (OWNER) ---");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama (Parent)");
            System.out.print("Pilih opsi (1-4): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addNewMenu();
                    break;
                case "2":
                    editMenuPrice();
                    break;
                case "3":
                    deleteMenu();
                    break;
                case "4":
                    inOwnerMenu = false; // Navigasi kembali ke menu parent sebelumnya
                    break;
                default:
                    System.out.println("Pilihan salah! Masukkan angka 1-4.");
            }
        }
    }

    // Aksi Pemilik: Tambah Menu
    private static void addNewMenu() {
        System.out.println("\n--- TAMBAH MENU BARU ---");
        System.out.print("Masukkan Nama Menu Baru: ");
        String name = scanner.nextLine();
        
        double price = 0;
        while (true) {
            System.out.print("Masukkan Harga Menu: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price > 0) break;
                System.out.println("Harga harus di atas 0!");
            } catch (NumberFormatException e) {
                System.out.println("Input salah! Harga harus berupa angka.");
            }
        }

        String category = "";
        while (true) {
            System.out.print("Masukkan Kategori (Makanan/Minuman): ");
            category = scanner.nextLine();
            if (category.equalsIgnoreCase("Makanan") || category.equalsIgnoreCase("Minuman")) {
                category = category.equalsIgnoreCase("Makanan") ? "Makanan" : "Minuman";
                break;
            }
            System.out.println("Kategori salah! Sistem hanya menerima kata 'Makanan' atau 'Minuman'.");
        }

        // Konfirmasi Layar Monitor sebelum manipulasi data
        System.out.print("Apakah Anda yakin ingin menambahkan menu ini? (Ya/Tidak): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Ya")) {
            menuList.add(new MenuItem(name, price, category));
            System.out.println("Menu baru sukses terdaftar!");
        } else {
            System.out.println("Penambahan menu dibatalkan.");
        }
    }

    // Aksi Pemilik: Ubah Harga Berdasarkan Nomor Urut Menu
    private static void editMenuPrice() {
        while (true) {
            displayMenu();
            System.out.print("Masukkan nomor menu yang ingin diubah harganya: ");
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                if (index >= 0 && index < menuList.size()) {
                    MenuItem item = menuList.get(index);
                    System.out.print("Masukkan Harga Baru untuk " + item.getName() + ": ");
                    double newPrice = Double.parseDouble(scanner.nextLine());

                    // Konfirmasi Aksi
                    System.out.print("Apakah Anda yakin ingin mengubah harga? (Ya/Tidak): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Ya")) {
                        item.setPrice(newPrice);
                        System.out.println("Harga menu sukses diperbarui!");
                    } else {
                        System.out.println("Perubahan harga dibatalkan.");
                    }
                    break;
                }
                System.out.println("Nomor menu tidak sesuai daftar!");
            } catch (NumberFormatException e) {
                System.out.println("Input salah! Gunakan angka nomor urut menu.");
            }
        }
    }

    // Aksi Pemilik: Hapus Menu Berdasarkan Nomor Urut
    private static void deleteMenu() {
        while (true) {
            displayMenu();
            System.out.print("Masukkan nomor menu yang ingin dihapus: ");
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                if (index >= 0 && index < menuList.size()) {
                    MenuItem item = menuList.get(index);
                    
                    // Konfirmasi Aksi
                    System.out.print("Apakah Anda yakin ingin MENGHAPUS '" + item.getName() + "'? (Ya/Tidak): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Ya")) {
                        menuList.remove(index);
                        System.out.println("Menu sukses dihapus dari sistem.");
                    } else {
                        System.out.println("Penghapusan menu dibatalkan.");
                    }
                    break;
                }
                System.out.println("Nomor menu tidak ditemukan!");
            } catch (NumberFormatException e) {
                System.out.println("Input salah! Masukkan kode angka.");
            }
        }
    }
}