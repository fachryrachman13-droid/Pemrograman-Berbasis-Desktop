import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

// ============================================================
// ABSTRACT CLASS - Kelas dasar abstrak untuk semua item menu
// ============================================================
abstract class MenuItem {
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    public String getKategori() { return kategori; }

    public abstract void tampilMenu();

    public String toFileString() {
        return getNama() + "|" + getHarga() + "|" + getKategori();
    }
}

// ============================================================
// SUBCLASS 1 - Makanan (Inheritance dari MenuItem)
// ============================================================
class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan() { return jenisMakanan; }

    @Override
    public void tampilMenu() {
        System.out.printf("  [MAKANAN] %-25s | Jenis: %-10s | Rp %,.0f\n", getNama(), jenisMakanan, getHarga());
    }

    @Override
    public String toFileString() {
        return "Makanan|" + getNama() + "|" + getHarga() + "|" + jenisMakanan;
    }
}

// ============================================================
// SUBCLASS 2 - Minuman (Inheritance dari MenuItem)
// ============================================================
class Minuman extends MenuItem {
    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman() { return jenisMinuman; }

    @Override
    public void tampilMenu() {
        System.out.printf("  [MINUMAN] %-25s | Jenis: %-10s | Rp %,.0f\n", getNama(), jenisMinuman, getHarga());
    }

    @Override
    public String toFileString() {
        return "Minuman|" + getNama() + "|" + getHarga() + "|" + jenisMinuman;
    }
}

// ============================================================
// SUBCLASS 3 - Diskon (Inheritance dari MenuItem)
// ============================================================
class Diskon extends MenuItem {
    private double persenDiskon;

    public Diskon(String nama, double harga, double persenDiskon) {
        super(nama, harga, "Diskon");
        this.persenDiskon = persenDiskon;
    }

    public double getPersenDiskon() { return persenDiskon; }
    public double getHargaSetelahDiskon() { return getHarga() * (1 - persenDiskon / 100); }

    @Override
    public void tampilMenu() {
        System.out.printf("  [DISKON]  %-25s | Diskon: %.0f%%  | Harga Normal: Rp %,.0f -> Rp %,.0f\n",
                getNama(), persenDiskon, getHarga(), getHargaSetelahDiskon());
    }

    @Override
    public String toFileString() {
        return "Diskon|" + getNama() + "|" + getHarga() + "|" + persenDiskon;
    }
}

// ============================================================
// KELAS Menu - Mengelola semua item menu (ArrayList)
// ============================================================
class Menu {
    private ArrayList<MenuItem> daftarMenu = new ArrayList<>();
    private static final String FILE_MENU = "menu.txt";

    public void tambahItem(MenuItem item) {
        daftarMenu.add(item);
    }

    public void hapusItem(int index) {
        if (index >= 0 && index < daftarMenu.size()) {
            daftarMenu.remove(index);
        } else {
            throw new IndexOutOfBoundsException("Nomor menu tidak valid!");
        }
    }

    public MenuItem getItem(int index) {
        if (index >= 0 && index < daftarMenu.size()) {
            return daftarMenu.get(index);
        }
        throw new IndexOutOfBoundsException("Nomor menu tidak ditemukan!");
    }

    public ArrayList<MenuItem> getDaftarMenu() { return daftarMenu; }
    public int getJumlah() { return daftarMenu.size(); }

    public void tampilkanMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("  (Daftar menu masih kosong)");
            return;
        }
        System.out.println("\n--- DAFTAR MENU RESTORAN ---");
        int no = 1;
        for (MenuItem item : daftarMenu) {
            System.out.print("  " + no++ + ". ");
            item.tampilMenu();
        }
        System.out.println("----------------------------");
    }

    public void simpanKeFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_MENU))) {
            for (MenuItem item : daftarMenu) {
                pw.println(item.toFileString());
            }
            System.out.println("[INFO] Menu berhasil disimpan ke file '" + FILE_MENU + "'.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan menu: " + e.getMessage());
        }
    }

    public void muatDariFile() {
        File file = new File(FILE_MENU);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_MENU))) {
            String line;
            daftarMenu.clear();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                String tipe = parts[0];
                String nama = parts[1];
                double harga = Double.parseDouble(parts[2]);
                String ekstra = parts[3];

                switch (tipe) {
                    case "Makanan": daftarMenu.add(new Makanan(nama, harga, ekstra)); break;
                    case "Minuman": daftarMenu.add(new Minuman(nama, harga, ekstra)); break;
                    case "Diskon":  daftarMenu.add(new Diskon(nama, harga, Double.parseDouble(ekstra))); break;
                }
            }
            System.out.println("[INFO] Menu berhasil dimuat dari file '" + FILE_MENU + "'.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal memuat menu: " + e.getMessage());
        }
    }
}

// ============================================================
// KELAS Pesanan - Mencatat pesanan pelanggan
// ============================================================
class Pesanan {
    private ArrayList<MenuItem> itemDipesan = new ArrayList<>();
    private ArrayList<Integer> jumlahItem = new ArrayList<>();
    private static final String FILE_STRUK = "struk_pesanan.txt";

    public void tambahPesanan(MenuItem item, int jumlah) {
        for (int i = 0; i < itemDipesan.size(); i++) {
            if (itemDipesan.get(i).getNama().equalsIgnoreCase(item.getNama())) {
                jumlahItem.set(i, jumlahItem.get(i) + jumlah);
                return;
            }
        }
        itemDipesan.add(item);
        jumlahItem.add(jumlah);
    }

    public boolean isEmpty() { return itemDipesan.isEmpty(); }

    public double hitungTotal() {
        double total = 0;
        for (int i = 0; i < itemDipesan.size(); i++) {
            MenuItem item = itemDipesan.get(i);
            double harga = (item instanceof Diskon)
                    ? ((Diskon) item).getHargaSetelahDiskon()
                    : item.getHarga();
            total += harga * jumlahItem.get(i);
        }
        return total;
    }

    public void cetakStruk() {
        System.out.println("\n============================================================");
        System.out.println("                   STRUK PESANAN PELANGGAN                  ");
        System.out.println("============================================================");
        System.out.printf("  %-28s %-5s %-12s %s\n", "Item", "Qty", "Harga Satuan", "Subtotal");
        System.out.println("  ----------------------------------------------------------");

        for (int i = 0; i < itemDipesan.size(); i++) {
            MenuItem item = itemDipesan.get(i);
            double harga = (item instanceof Diskon)
                    ? ((Diskon) item).getHargaSetelahDiskon()
                    : item.getHarga();
            int qty = jumlahItem.get(i);
            System.out.printf("  %-28s %-5d Rp %,8.0f  Rp %,10.0f\n",
                    item.getNama(), qty, harga, harga * qty);
        }

        double subtotal = hitungTotal();
        double diskon = (subtotal > 100000) ? subtotal * 0.10 : 0;
        double pajak = (subtotal - diskon) * 0.10;
        double grandTotal = subtotal - diskon + pajak;

        System.out.println("  ----------------------------------------------------------");
        System.out.printf("  %-40s: Rp %,10.0f\n", "Subtotal", subtotal);
        if (diskon > 0) System.out.printf("  %-40s:-Rp %,10.0f\n", "Diskon 10% (pembelian >100rb)", diskon);
        System.out.printf("  %-40s: Rp %,10.0f\n", "Pajak (10%)", pajak);
        System.out.println("  ----------------------------------------------------------");
        System.out.printf("  %-40s: Rp %,10.0f\n", "GRAND TOTAL", grandTotal);
        System.out.println("============================================================");
        System.out.println("         Terima kasih! Selamat menikmati hidangan Anda.     ");
        System.out.println("============================================================");

        simpanStrukKeFile(subtotal, diskon, pajak, grandTotal);
    }

    private void simpanStrukKeFile(double subtotal, double diskon, double pajak, double grandTotal) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_STRUK, true))) {
            pw.println("=== STRUK PESANAN ===");
            for (int i = 0; i < itemDipesan.size(); i++) {
                MenuItem item = itemDipesan.get(i);
                double harga = (item instanceof Diskon) ? ((Diskon) item).getHargaSetelahDiskon() : item.getHarga();
                pw.printf("%s x%d = Rp %.0f\n", item.getNama(), jumlahItem.get(i), harga * jumlahItem.get(i));
            }
            pw.printf("Subtotal: Rp %.0f\n", subtotal);
            if (diskon > 0) pw.printf("Diskon: -Rp %.0f\n", diskon);
            pw.printf("Pajak: Rp %.0f\n", pajak);
            pw.printf("Grand Total: Rp %.0f\n", subtotal - diskon + pajak);
            pw.println("=====================\n");
            System.out.println("[INFO] Struk disimpan ke file '" + FILE_STRUK + "'.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan struk: " + e.getMessage());
        }
    }
}

// ============================================================
// MAIN CLASS - RestoranApp
// ============================================================
public class RestoranApp {
    private static Menu menu = new Menu();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        menu.muatDariFile();
        if (menu.getJumlah() == 0) initMenuDefault();

        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("      APLIKASI MANAJEMEN RESTORAN       ");
            System.out.println("========================================");
            System.out.println("1. Tambah Item Menu Baru");
            System.out.println("2. Tampilkan Menu Restoran");
            System.out.println("3. Buat Pesanan Pelanggan");
            System.out.println("4. Keluar");
            System.out.print("Pilih opsi (1-4): ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1": tambahMenu(); break;
                case "2": menu.tampilkanMenu(); break;
                case "3": buatPesanan(); break;
                case "4":
                    menu.simpanKeFile();
                    System.out.println("Sampai jumpa!");
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Masukkan angka 1-4.");
            }
        }
    }

    private static void initMenuDefault() {
        menu.tambahItem(new Makanan("Nasi Goreng Spesial", 25000, "Nasi"));
        menu.tambahItem(new Makanan("Ayam Bakar Taliwang", 35000, "Ayam"));
        menu.tambahItem(new Makanan("Mie Goreng Seafood", 28000, "Mie"));
        menu.tambahItem(new Minuman("Es Teh Manis", 6000, "Teh"));
        menu.tambahItem(new Minuman("Jus Alpukat", 15000, "Jus"));
        menu.tambahItem(new Diskon("Paket Hemat Siang", 50000, 20));
    }

    private static void tambahMenu() {
        System.out.println("\n--- TAMBAH ITEM MENU BARU ---");
        System.out.println("1. Makanan");
        System.out.println("2. Minuman");
        System.out.println("3. Diskon");
        System.out.print("Pilih tipe item (1-3): ");
        String tipe = scanner.nextLine();

        System.out.print("Nama item: ");
        String nama = scanner.nextLine();

        double harga = 0;
        while (true) {
            System.out.print("Harga: ");
            try {
                harga = Double.parseDouble(scanner.nextLine());
                if (harga > 0) break;
                System.out.println("Harga harus lebih dari 0!");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }

        switch (tipe) {
            case "1":
                System.out.print("Jenis makanan (contoh: Nasi/Mie/Ayam): ");
                String jenisMakanan = scanner.nextLine();
                menu.tambahItem(new Makanan(nama, harga, jenisMakanan));
                break;
            case "2":
                System.out.print("Jenis minuman (contoh: Jus/Teh/Kopi): ");
                String jenisMinuman = scanner.nextLine();
                menu.tambahItem(new Minuman(nama, harga, jenisMinuman));
                break;
            case "3":
                double persen = 0;
                while (true) {
                    System.out.print("Persen diskon (contoh: 20 untuk 20%): ");
                    try {
                        persen = Double.parseDouble(scanner.nextLine());
                        if (persen > 0 && persen < 100) break;
                        System.out.println("Diskon harus antara 1-99%!");
                    } catch (NumberFormatException e) {
                        System.out.println("Input harus berupa angka!");
                    }
                }
                menu.tambahItem(new Diskon(nama, harga, persen));
                break;
            default:
                System.out.println("Tipe tidak valid, item tidak ditambahkan.");
                return;
        }
        System.out.println("Item berhasil ditambahkan ke menu!");
    }

    private static void buatPesanan() {
        Pesanan pesanan = new Pesanan();
        boolean memesan = true;

        while (memesan) {
            menu.tampilkanMenu();
            System.out.println("  Ketik nomor item untuk dipesan, atau '0' untuk selesai.");
            System.out.print("  Nomor item: ");

            try {
                int nomor = Integer.parseInt(scanner.nextLine());
                if (nomor == 0) {
                    memesan = false;
                    break;
                }

                MenuItem item = menu.getItem(nomor - 1);
                item.tampilMenu();

                int jumlah = 0;
                while (true) {
                    System.out.print("  Jumlah: ");
                    try {
                        jumlah = Integer.parseInt(scanner.nextLine());
                        if (jumlah > 0) break;
                        System.out.println("  Jumlah minimal 1!");
                    } catch (NumberFormatException e) {
                        System.out.println("  Input harus angka!");
                    }
                }

                pesanan.tambahPesanan(item, jumlah);
                System.out.println("  Berhasil ditambahkan ke pesanan.");

            } catch (NumberFormatException e) {
                System.out.println("  Input tidak valid! Masukkan angka nomor menu.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("  " + e.getMessage());
            }
        }

        if (pesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan. Kembali ke menu utama.");
            return;
        }

        pesanan.cetakStruk();
        System.out.print("\nTekan ENTER untuk kembali...");
        scanner.nextLine();
    }
}
