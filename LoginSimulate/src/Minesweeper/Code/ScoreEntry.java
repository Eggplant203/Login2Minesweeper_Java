package LoginSimulate.src.Minesweeper.Code;

public class ScoreEntry implements Comparable<ScoreEntry> {
    private String username;
    private long time; // Time stored in milliseconds

    // Constructor that takes username and time in milliseconds
    public ScoreEntry(String username, long time) {
        this.username = username;
        this.time = time;
    }

    // Constructor that takes username and time as a formatted string (HH:mm:ss.SSS)
    public ScoreEntry(String username, String timeString) {
        this.username = username;
        this.time = parseTimeString(timeString);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTime() {
        return time;
    }

    // Method to parse a time string into milliseconds
    private long parseTimeString(String timeString) {
        String[] timeParts = timeString.split("[:\\.]");
        
        // Ensure that we have the expected number of parts
        if (timeParts.length != 4) {
            throw new IllegalArgumentException("Time string must be in format HH:mm:ss.SSS");
        }
    
        long hours = Long.parseLong(timeParts[0]);
        long minutes = Long.parseLong(timeParts[1]);
        long seconds = Long.parseLong(timeParts[2]);
        long milliseconds = Long.parseLong(timeParts[3]);
    
        // Calculate total milliseconds
        return (hours * 3600000) + (minutes * 60000) + (seconds * 1000) + milliseconds;
    }

    // Method to convert milliseconds to a formatted time string (HH:mm:ss.SSS)
    public String formatTime() {
        long totalMilliseconds = time;
        long hours = totalMilliseconds / 3600000;
        totalMilliseconds %= 3600000;
        long minutes = totalMilliseconds / 60000;
        totalMilliseconds %= 60000;
        long seconds = totalMilliseconds / 1000;
        long milliseconds = totalMilliseconds % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Long.compare(this.time, other.time);
    }
}
