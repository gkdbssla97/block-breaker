package model.audio;

import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class GameAudio {
    public GameAudio() {
    }

    //블럭충돌 재생 메서드
    public static void gameStartAudio() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/sourceFile/gameStart.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            // clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }

    //블럭충돌 재생 메서드
    public static void blockCollisionAudio() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/sourceFile/blockCollision.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            // clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }
    //게임종료 재생 메서드
    public static void gameEndAudio() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/sourceFile/blockCollision.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            // clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.start();
        } catch (Exception e) {
            System.err.println("Put the music.wav file in the sound folder if you want to play background music, only optional!");
        }
    }
}

