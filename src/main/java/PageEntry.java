public class PageEntry implements Comparable<PageEntry> {
    public String getPdfName() {
        return pdfName;
    }

    private final String pdfName;

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }
    @Override //реализация интерфейса Comparable;
    public int compareTo(PageEntry o) {
        return this.count - o.count;
    }
}