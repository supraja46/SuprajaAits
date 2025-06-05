import java.util.Stack;
public class BrowseHistory {
    private final Stack<String> backStack = new Stack<>();
    private final Stack<String> forwardStack = new Stack<>();
    private String currentPage = "Home";
    public void visit(String url) {
        backStack.push(currentPage);
        currentPage = url;
        forwardStack.clear();
        log("Visited: " + url);
    }
    public void back() {
        if (!backStack.isEmpty()) {
            forwardStack.push(currentPage);
            currentPage = backStack.pop();
            log("Back to: " + currentPage);
        } else {
            log("No pages to go back to.");
        }
    }
    public void forward() {
        if (!forwardStack.isEmpty()) {
            backStack.push(currentPage);
            currentPage = forwardStack.pop();
            log("Forward to: " + currentPage);
        } else {
            log("No pages to go forward to.");
        }
    }
    private void log(String message) {
        System.out.println(message);
    }
    public void showCurrentPage() {
        System.out.println("Current page: " + currentPage);
    }
    public static void main(String[] args) {
        BrowseHistory browser = new BrowseHistory();
        browser.showCurrentPage();
        browser.visit("https://google.com");
        browser.visit("https://github.com");
        browser.visit("https://stackoverflow.com");
        browser.back();
        browser.back();
        browser.forward();
        browser.visit("https://open.com");
        browser.forward();
        browser.showCurrentPage();
    }
}
