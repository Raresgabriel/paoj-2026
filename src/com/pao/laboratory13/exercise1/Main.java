package com.pao.laboratory13.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private enum State { INIT, AUTH, OPEN, CLOSED }

    private State state = State.INIT;
    private int historyCount = 0;

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String first = nextNonEmpty(br);
        if (first == null) return;
        int q = Integer.parseInt(first);
        for (int i = 0; i < q; i++) {
            String line = nextNonEmpty(br);
            if (line == null) return;
            System.out.println(handle(line));
        }
    }

    private String handle(String line) {
        String[] tok = line.split("\\s+", 2);
        String cmd = tok[0].toUpperCase();
        String rest = tok.length > 1 ? tok[1] : "";

        switch (cmd) {
            case "AUTH": return handleAuth(rest);
            case "OPEN": return handleOpen(rest);
            case "SEND": return handleSend(rest);
            case "BROADCAST": return handleBroadcast(rest);
            case "HISTORY": return handleHistory(rest);
            case "CLOSE": return handleClose(rest);
            default: return "ERR E_PARSE UNKNOWN_COMMAND";
        }
    }

    private String handleAuth(String rest) {
        if (state == State.CLOSED) return "ERR E_STATE CLOSED";
        if (rest.isEmpty()) return "ERR E_PARSE AUTH";
        String user = rest.split("\\s+")[0];
        state = State.AUTH;
        historyCount = 0;
        return "OK AUTH user=" + user;
    }

    private String handleOpen(String rest) {
        if (state == State.CLOSED) return "ERR E_STATE CLOSED";
        if (!rest.isEmpty()) return "ERR E_PARSE OPEN";
        if (state == State.OPEN) return "ERR E_STATE ALREADY_OPEN";
        if (state == State.INIT) return "ERR E_STATE NOT_OPEN";
        state = State.OPEN;
        return "OK OPEN";
    }

    private String handleSend(String rest) {
        if (state == State.CLOSED) return "ERR E_STATE CLOSED";
        if (rest.isEmpty()) return "ERR E_PARSE SEND";
        if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
        historyCount++;
        return "OK OPEN sent";
    }

    private String handleBroadcast(String rest) {
        if (state == State.CLOSED) return "ERR E_STATE CLOSED";
        if (rest.isEmpty()) return "ERR E_PARSE BROADCAST";
        if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
        historyCount++;
        return "OK OPEN broadcast";
    }

    private String handleHistory(String rest) {
        if (state == State.CLOSED) return "ERR E_STATE CLOSED";
        if (!rest.isEmpty()) return "ERR E_PARSE HISTORY";
        if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
        return "OK OPEN history=" + historyCount;
    }

    private String handleClose(String rest) {
        if (state == State.CLOSED) return "ERR E_STATE CLOSED";
        if (!rest.isEmpty()) return "ERR E_PARSE CLOSE";
        if (state != State.OPEN) return "ERR E_STATE NOT_OPEN";
        state = State.CLOSED;
        return "OK CLOSED";
    }

    private static String nextNonEmpty(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) return line.trim();
        }
        return null;
    }
}
