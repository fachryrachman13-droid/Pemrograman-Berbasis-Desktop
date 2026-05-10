/**
 * ============================================================
 *  Kelas    : Menu
 *  Deskripsi: Merepresentasikan satu item menu di restoran.
 *             Setiap objek Menu menyimpan nama, harga, dan
 *             kategori (Makanan / Minuman).
 * ============================================================
 */
public class Menu {

    // ── Atribut / Instance Variables ────────────────────────
    private String nama;       // identifier bertipe String
    private double harga;      // identifier bertipe double (primitif)
    private String kategori;   // "Makanan" atau "Minuman"

    // ── Constructor ─────────────────────────────────────────
    /**
     * Membuat objek Menu baru.
     *
     * @param nama      Nama item menu
     * @param harga     Harga dalam Rupiah
     * @param kategori  "Makanan" atau "Minuman"
     */
    public Menu(String nama, double harga, String kategori) {
        this.nama     = nama;
        this.harga    = harga;
        this.kategori = kategori;
    }

    // ── Getter Methods ──────────────────────────────────────
    public String getNama()     { return nama;     }
    public double getHarga()    { return harga;    }
    public String getKategori() { return kategori; }

    // ── toString (opsional, untuk debugging) ────────────────
    @Override
    public String toString() {
        return String.format("%-20s | %-8s | Rp %,.0f", nama, kategori, harga);
    }
}
