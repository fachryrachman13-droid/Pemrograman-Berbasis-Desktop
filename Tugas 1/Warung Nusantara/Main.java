import java.util.Scanner;
/**
 * ============================================================
 *  Kelas    : Main
 *  Deskripsi: Kelas utama aplikasi kasir sederhana
 *             "Warung Nusantara".
 *
 *  Fitur    :
 *    1. Menampilkan daftar menu (makanan & minuman)
 *    2. Menerima pesanan pelanggan (maks. 4 menu)
 *    3. Menghitung total biaya + pajak + layanan + diskon
 *    4. Mencetak struk pembayaran
 *
 *  Struktur Keputusan yang Digunakan:
 *    - if                 (validasi input, hitung subtotal)
 *    - if-else            (cek diskon 10%)
 *    - if-else if         (pencarian minuman untuk BOGO)
 *    - switch-case        (pemilihan menu oleh pelanggan)
 *    - nested if          (BOGO: cek subtotal dulu, lalu cek
 *                          apakah ada minuman dalam pesanan)
 * ============================================================
 */
public class Main {

    // ══════════════════════════════════════════════════════
    //  KONSTANTA (keyword: static final)
    // ══════════════════════════════════════════════════════
    static final int    MAKS_MENU        = 8;
    static final int    MAKS_ORDER       = 4;
    static final double TARIF_PAJAK      = 0.10;   // 10 %
    static final double BIAYA_LAYANAN    = 20_000;  // Rp 20.000
    static final double TARIF_DISKON     = 0.10;   // 10 %
    static final double MIN_TOTAL_DISKON = 100_000; // syarat diskon 10%
    static final double MIN_TOTAL_BOGO   = 50_000;  // syarat promo BOGO

    // ══════════════════════════════════════════════════════
    //  ARRAY DATA MENU  (8 objek Menu)
    // ══════════════════════════════════════════════════════
    static Menu[] daftarMenu = new Menu[MAKS_MENU];

    // ══════════════════════════════════════════════════════
    //  ARRAY DATA PESANAN  (maks. 4 slot)
    // ══════════════════════════════════════════════════════
    static String[] namaOrder     = new String[MAKS_ORDER]; // nama menu yg dipesan
    static int[]    jumlahOrder   = new int   [MAKS_ORDER]; // jumlah porsi
    static double[] hargaOrder    = new double[MAKS_ORDER]; // harga satuan
    static String[] kategoriOrder = new String[MAKS_ORDER]; // kategori item
    static int      totalItemDipesan = 0;                   // counter item

    // ══════════════════════════════════════════════════════
    //  MAIN METHOD
    // ══════════════════════════════════════════════════════
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        inisialisasiMenu();

        cetakGaris('=', 46);
        System.out.println("    SELAMAT DATANG DI WARUNG NUSANTARA");
        System.out.println("       Jl. Nusantara No. 17, Jakarta");
        cetakGaris('=', 46);

        tampilkanMenu();
        terimaOrder(sc);

        double subtotal = hitungSubtotal();
        cetakStruk(subtotal);

        sc.close();
    }

    // ══════════════════════════════════════════════════════
    //  METHOD 1 : inisialisasiMenu()
    //  Mengisi array daftarMenu dengan objek-objek Menu.
    //  Tiap baris = new Menu(nama, harga, kategori)
    // ══════════════════════════════════════════════════════
    static void inisialisasiMenu() {
        // ── Makanan (index 0 – 3) ──────────────────────
        daftarMenu[0] = new Menu("Nasi Padang",   25_000, "Makanan");
        daftarMenu[1] = new Menu("Ayam Bakar",    30_000, "Makanan");
        daftarMenu[2] = new Menu("Mie Goreng",    20_000, "Makanan");
        daftarMenu[3] = new Menu("Soto Ayam",     22_000, "Makanan");

        // ── Minuman (index 4 – 7) ──────────────────────
        daftarMenu[4] = new Menu("Es Teh Manis",   8_000, "Minuman");
        daftarMenu[5] = new Menu("Jus Alpukat",   15_000, "Minuman");
        daftarMenu[6] = new Menu("Es Jeruk",      10_000, "Minuman");
        daftarMenu[7] = new Menu("Kopi Hitam",    12_000, "Minuman");
    }

    // ══════════════════════════════════════════════════════
    //  METHOD 2 : tampilkanMenu()
    //  Menampilkan menu dikelompokkan berdasarkan kategori.
    //  Mengakses array daftarMenu dengan indeks eksplisit
    //  (tidak menggunakan loop sesuai petunjuk tugas).
    // ══════════════════════════════════════════════════════
    static void tampilkanMenu() {
        System.out.println();
        cetakGaris('=', 46);
        System.out.printf("  %-4s %-22s %s%n", "No.", "Nama Menu", "Harga");
        cetakGaris('-', 46);

        // ── Kelompok MAKANAN ──────────────────────────
        System.out.println("  [ MAKANAN ]");
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "1.", daftarMenu[0].getNama(), daftarMenu[0].getHarga());
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "2.", daftarMenu[1].getNama(), daftarMenu[1].getHarga());
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "3.", daftarMenu[2].getNama(), daftarMenu[2].getHarga());
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "4.", daftarMenu[3].getNama(), daftarMenu[3].getHarga());

        System.out.println();

        // ── Kelompok MINUMAN ──────────────────────────
        System.out.println("  [ MINUMAN ]");
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "5.", daftarMenu[4].getNama(), daftarMenu[4].getHarga());
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "6.", daftarMenu[5].getNama(), daftarMenu[5].getHarga());
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "7.", daftarMenu[6].getNama(), daftarMenu[6].getHarga());
        System.out.printf("  %-4s %-22s Rp %,.0f%n",
            "8.", daftarMenu[7].getNama(), daftarMenu[7].getHarga());

        cetakGaris('=', 46);

        // Info promo
        System.out.println("  * Promo BOGO (Beli 1 Gratis 1) minuman");
        System.out.println("    jika total pesanan > Rp 50.000");
        System.out.println("  * Diskon 10% jika total pesanan > Rp 100.000");
        cetakGaris('=', 46);
    }

    // ══════════════════════════════════════════════════════
    //  METHOD 3 : terimaOrder(Scanner)
    //  Menerima input pesanan pelanggan untuk 4 slot.
    //  Menggunakan NESTED IF untuk membatasi maks. 4 menu
    //  tanpa loop – setiap slot ditangani secara eksplisit.
    // ══════════════════════════════════════════════════════
    static void terimaOrder(Scanner sc) {
        System.out.println("\n  ====== INPUT PESANAN ======");
        System.out.println("  Masukkan nomor menu (1-8)");
        System.out.println("  Ketik 0 untuk mengakhiri pesanan");
        System.out.println("  (Maksimal 4 jenis menu)\n");

        // ── SLOT 1 ────────────────────────────────────
        System.out.print("  Pesanan ke-1, Nomor menu [0=selesai] : ");
        int nomor1 = sc.nextInt();

        if (nomor1 == 0) {
            System.out.println("  Tidak ada pesanan. Program selesai.");
            System.exit(0);

        } else if (nomor1 >= 1 && nomor1 <= 8) {
            System.out.print("  Pesanan ke-1, Jumlah porsi          : ");
            int jumlah1 = sc.nextInt();
            prosesOrder(nomor1, jumlah1, 0);
            totalItemDipesan = 1;

            // ── SLOT 2 ────────────────────────────────
            System.out.print("\n  Pesanan ke-2, Nomor menu [0=selesai] : ");
            int nomor2 = sc.nextInt();

            if (nomor2 != 0 && nomor2 >= 1 && nomor2 <= 8) {
                System.out.print("  Pesanan ke-2, Jumlah porsi          : ");
                int jumlah2 = sc.nextInt();
                prosesOrder(nomor2, jumlah2, 1);
                totalItemDipesan = 2;

                // ── SLOT 3 ────────────────────────────
                System.out.print("\n  Pesanan ke-3, Nomor menu [0=selesai] : ");
                int nomor3 = sc.nextInt();

                if (nomor3 != 0 && nomor3 >= 1 && nomor3 <= 8) {
                    System.out.print("  Pesanan ke-3, Jumlah porsi          : ");
                    int jumlah3 = sc.nextInt();
                    prosesOrder(nomor3, jumlah3, 2);
                    totalItemDipesan = 3;

                    // ── SLOT 4 ────────────────────────
                    System.out.print("\n  Pesanan ke-4, Nomor menu [0=selesai] : ");
                    int nomor4 = sc.nextInt();

                    if (nomor4 != 0 && nomor4 >= 1 && nomor4 <= 8) {
                        System.out.print("  Pesanan ke-4, Jumlah porsi          : ");
                        int jumlah4 = sc.nextInt();
                        prosesOrder(nomor4, jumlah4, 3);
                        totalItemDipesan = 4;
                    }
                    // if nomor4 == 0 → pemesanan berhenti di sini
                }
                // if nomor3 == 0 → pemesanan berhenti di sini
            }
            // if nomor2 == 0 → pemesanan berhenti di sini

        } else {
            System.out.println("  Nomor menu tidak valid. Program selesai.");
            System.exit(0);
        }

        System.out.println("\n  Pemesanan selesai. "
            + totalItemDipesan + " jenis menu dipesan.");
    }

    // ══════════════════════════════════════════════════════
    //  METHOD 4 : prosesOrder(int, int, int)
    //  Menyimpan data menu yang dipilih ke dalam array
    //  pesanan menggunakan SWITCH-CASE.
    // ══════════════════════════════════════════════════════
    static void prosesOrder(int nomorMenu, int jumlah, int slot) {

        // SWITCH-CASE: menentukan data menu berdasarkan nomor
        switch (nomorMenu) {
            case 1:
                namaOrder[slot]     = daftarMenu[0].getNama();
                hargaOrder[slot]    = daftarMenu[0].getHarga();
                kategoriOrder[slot] = daftarMenu[0].getKategori();
                break;
            case 2:
                namaOrder[slot]     = daftarMenu[1].getNama();
                hargaOrder[slot]    = daftarMenu[1].getHarga();
                kategoriOrder[slot] = daftarMenu[1].getKategori();
                break;
            case 3:
                namaOrder[slot]     = daftarMenu[2].getNama();
                hargaOrder[slot]    = daftarMenu[2].getHarga();
                kategoriOrder[slot] = daftarMenu[2].getKategori();
                break;
            case 4:
                namaOrder[slot]     = daftarMenu[3].getNama();
                hargaOrder[slot]    = daftarMenu[3].getHarga();
                kategoriOrder[slot] = daftarMenu[3].getKategori();
                break;
            case 5:
                namaOrder[slot]     = daftarMenu[4].getNama();
                hargaOrder[slot]    = daftarMenu[4].getHarga();
                kategoriOrder[slot] = daftarMenu[4].getKategori();
                break;
            case 6:
                namaOrder[slot]     = daftarMenu[5].getNama();
                hargaOrder[slot]    = daftarMenu[5].getHarga();
                kategoriOrder[slot] = daftarMenu[5].getKategori();
                break;
            case 7:
                namaOrder[slot]     = daftarMenu[6].getNama();
                hargaOrder[slot]    = daftarMenu[6].getHarga();
                kategoriOrder[slot] = daftarMenu[6].getKategori();
                break;
            case 8:
                namaOrder[slot]     = daftarMenu[7].getNama();
                hargaOrder[slot]    = daftarMenu[7].getHarga();
                kategoriOrder[slot] = daftarMenu[7].getKategori();
                break;
            default:
                System.out.println("  [!] Nomor menu tidak tersedia.");
                namaOrder[slot]     = null;
                hargaOrder[slot]    = 0;
                kategoriOrder[slot] = "";
                jumlah              = 0;
        }

        jumlahOrder[slot] = jumlah;

        // IF: hanya tampilkan konfirmasi jika nama valid
        if (namaOrder[slot] != null && jumlah > 0) {
            double totalItem = hargaOrder[slot] * jumlah;
            System.out.printf("  [+] %-20s x%d = Rp %,.0f%n",
                namaOrder[slot], jumlah, totalItem);
        }
    }

    // ══════════════════════════════════════════════════════
    //  METHOD 5 : hitungSubtotal()
    //  Menjumlahkan harga × jumlah untuk setiap slot
    //  pesanan. Menggunakan IF (bukan loop) untuk memeriksa
    //  apakah slot terisi (tidak null).
    //  Return tipe: double
    // ══════════════════════════════════════════════════════
    static double hitungSubtotal() {
        double subtotal = 0;

        if (namaOrder[0] != null) subtotal += hargaOrder[0] * jumlahOrder[0];
        if (namaOrder[1] != null) subtotal += hargaOrder[1] * jumlahOrder[1];
        if (namaOrder[2] != null) subtotal += hargaOrder[2] * jumlahOrder[2];
        if (namaOrder[3] != null) subtotal += hargaOrder[3] * jumlahOrder[3];

        return subtotal;
    }

    // ══════════════════════════════════════════════════════
    //  METHOD 6 : cetakStruk(double)
    //  Menghitung semua komponen biaya dan mencetak struk.
    //
    //  Struktur keputusan di method ini:
    //    - NESTED IF   → cek subtotal > 50rb, lalu cek
    //                    ada minuman dalam pesanan (BOGO)
    //    - IF-ELSE IF  → mencari minuman pertama di slot 0–3
    //    - IF-ELSE     → cek diskon 10% (subtotal > 100rb)
    //    - IF          → tampilkan baris BOGO / diskon
    //                    hanya jika berlaku
    // ══════════════════════════════════════════════════════
    static void cetakStruk(double subtotal) {

        // ── Variabel kalkulasi ────────────────────────
        double nilaiBogoDiskon = 0;
        String namaItemBogo    = "";
        double nilaiDiskon10   = 0;
        String infoBogo        = "";
        String infoDiskon      = "";

        // ════════════════════════════════════════
        //  NESTED IF : Promo BOGO
        //  Lapisan 1 : apakah subtotal > 50.000?
        //  Lapisan 2 : apakah ada Minuman dipesan?
        // ════════════════════════════════════════
        if (subtotal > MIN_TOTAL_BOGO) {

            // IF-ELSE IF : cari slot pertama yang berisi minuman
            if (namaOrder[0] != null && kategoriOrder[0].equals("Minuman")) {
                namaItemBogo    = namaOrder[0];
                nilaiBogoDiskon = hargaOrder[0]; // 1 porsi gratis
            } else if (namaOrder[1] != null && kategoriOrder[1].equals("Minuman")) {
                namaItemBogo    = namaOrder[1];
                nilaiBogoDiskon = hargaOrder[1];
            } else if (namaOrder[2] != null && kategoriOrder[2].equals("Minuman")) {
                namaItemBogo    = namaOrder[2];
                nilaiBogoDiskon = hargaOrder[2];
            } else if (namaOrder[3] != null && kategoriOrder[3].equals("Minuman")) {
                namaItemBogo    = namaOrder[3];
                nilaiBogoDiskon = hargaOrder[3];
            }
            // Jika tidak ada minuman → nilaiBogoDiskon tetap 0
        }
        // Jika subtotal ≤ 50.000 → tidak masuk if, BOGO tidak berlaku

        // Total setelah BOGO dikurangkan
        double totalSetelahBogo = subtotal - nilaiBogoDiskon;

        // ════════════════════════════════════════
        //  IF-ELSE : Diskon 10%
        //  Cek berdasarkan subtotal asli (sebelum BOGO)
        // ════════════════════════════════════════
        if (subtotal > MIN_TOTAL_DISKON) {
            nilaiDiskon10 = totalSetelahBogo * TARIF_DISKON;
            infoDiskon    = "Diskon 10% (total > Rp100rb)";
        } else {
            nilaiDiskon10 = 0; // tidak ada diskon
        }

        double totalSetelahDiskon = totalSetelahBogo - nilaiDiskon10;
        double biayaPajak         = totalSetelahDiskon * TARIF_PAJAK;
        double grandTotal         = totalSetelahDiskon + biayaPajak + BIAYA_LAYANAN;

        // ═══════════════════════════════════════════════
        //  CETAK STRUK
        // ═══════════════════════════════════════════════
        System.out.println();
        cetakGaris('=', 46);
        System.out.println("           STRUK PEMBAYARAN");
        System.out.println("          WARUNG NUSANTARA");
        cetakGaris('=', 46);
        System.out.printf("  %-22s %5s  %10s%n", "Item", "Qty", "Subtotal");
        cetakGaris('-', 46);

        // Cetak setiap slot yang terisi menggunakan IF
        if (namaOrder[0] != null) {
            System.out.printf("  %-22s %5d  Rp%,.0f%n",
                namaOrder[0], jumlahOrder[0], hargaOrder[0] * jumlahOrder[0]);
        }
        if (namaOrder[1] != null) {
            System.out.printf("  %-22s %5d  Rp%,.0f%n",
                namaOrder[1], jumlahOrder[1], hargaOrder[1] * jumlahOrder[1]);
        }
        if (namaOrder[2] != null) {
            System.out.printf("  %-22s %5d  Rp%,.0f%n",
                namaOrder[2], jumlahOrder[2], hargaOrder[2] * jumlahOrder[2]);
        }
        if (namaOrder[3] != null) {
            System.out.printf("  %-22s %5d  Rp%,.0f%n",
                namaOrder[3], jumlahOrder[3], hargaOrder[3] * jumlahOrder[3]);
        }

        cetakGaris('-', 46);
        System.out.printf("  %-32s Rp%,.0f%n", "Subtotal Pesanan :", subtotal);

        // IF : tampilkan baris BOGO hanya jika berlaku
        if (nilaiBogoDiskon > 0) {
            System.out.printf("  %-32s Rp%,.0f%n",
                "Promo BOGO (" + namaItemBogo + ") :", nilaiBogoDiskon);
            System.out.printf("  %-32s Rp%,.0f%n",
                "Subtotal setelah BOGO :", totalSetelahBogo);
        }

        // IF : tampilkan baris diskon 10% hanya jika berlaku
        if (nilaiDiskon10 > 0) {
            System.out.printf("  %-32s Rp%,.0f%n", infoDiskon + " :", nilaiDiskon10);
            System.out.printf("  %-32s Rp%,.0f%n",
                "Subtotal setelah Diskon :", totalSetelahDiskon);
        }

        cetakGaris('-', 46);
        System.out.printf("  %-32s Rp%,.0f%n", "Pajak 10% :", biayaPajak);
        System.out.printf("  %-32s Rp%,.0f%n", "Biaya Layanan :", BIAYA_LAYANAN);
        cetakGaris('=', 46);
        System.out.printf("  %-32s Rp%,.0f%n", "TOTAL PEMBAYARAN :", grandTotal);
        cetakGaris('=', 46);

        // IF-ELSE IF-ELSE : pesan penutup berdasarkan besar pembayaran
        if (grandTotal > 200_000) {
            System.out.println("  Terima kasih atas kepercayaan Anda!");
            System.out.println("  Dapatkan voucher spesial di kunjungan berikutnya.");
        } else if (grandTotal > 100_000) {
            System.out.println("  Terima kasih! Nikmati hidangan Anda.");
        } else {
            System.out.println("  Terima kasih sudah berkunjung!");
        }

        cetakGaris('=', 46);
        System.out.println();
    }

    // ══════════════════════════════════════════════════════
    //  METHOD UTILITAS : cetakGaris(char, int)
    //  Mencetak garis pemisah berulang sebanyak 'panjang'
    //  karakter. (Ini satu-satunya tempat loop digunakan,
    //  hanya untuk keperluan tampilan/formatting.)
    // ══════════════════════════════════════════════════════
    static void cetakGaris(char karakter, int panjang) {
        for (int i = 0; i < panjang; i++) System.out.print(karakter);
        System.out.println();
    }
}
