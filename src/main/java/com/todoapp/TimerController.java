package main.java.com.todoapp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class TimerController {
    private Timeline timeline;
    private int seconds;
    private int targetSeconds;
    private boolean isCountdown;
    private Label timerLabel;
    private Runnable onTimerComplete;

    public TimerController(Label timerLabel) {
        this.timerLabel = timerLabel;
        this.seconds = 0;
        this.targetSeconds = 0;
        this.isCountdown = false;
    }

    public void startStopwatch() {
        stop();
        seconds = 0;
        isCountdown = false;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            updateDisplay();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void startCountdown(int minutes) {
        stop();
        targetSeconds = minutes * 60;
        seconds = targetSeconds;
        isCountdown = true;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds--;
            updateDisplay();
            if (seconds <= 0) {
                stop();
                playAlarm();
                if (onTimerComplete != null) {
                    onTimerComplete.run();
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void pause() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    public void resume() {
        if (timeline != null) {
            timeline.play();
        }
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
        seconds = 0;
        updateDisplay();
    }

    public boolean isRunning() {
        return timeline != null && timeline.getStatus() == Animation.Status.RUNNING;
    }

    private void updateDisplay() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
    }

    private void playAlarm() {
        try {
            AudioClip alarm = new AudioClip(getClass().getResource("/com/todoapp/alarm.wav").toString());
            alarm.setVolume(0.3);
            alarm.play();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir la alarma");
        }
    }

    public void setOnTimerComplete(Runnable callback) {
        this.onTimerComplete = callback;
    }

    public String getFormattedTime() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}

