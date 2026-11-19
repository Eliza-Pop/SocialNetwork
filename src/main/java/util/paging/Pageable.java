package util.paging;

public final class Pageable {

    private final int pageNumber;
    private final int  pageSize;

    public Pageable(int pageNumber, int pageSize, int offset) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getOffset()
    {
        return (long) pageNumber*pageSize ;
    }
}
