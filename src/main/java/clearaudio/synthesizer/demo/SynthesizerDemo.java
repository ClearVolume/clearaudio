package clearaudio.synthesizer.demo;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import javax.sound.sampled.LineUnavailableException;

import clearaudio.sound.SoundOut;
import clearaudio.synthesizer.Synthesizer;
import clearaudio.synthesizer.filters.LowPassFilter;
import clearaudio.synthesizer.filters.NoiseFilter;
import clearaudio.synthesizer.filters.ReverbFilter;
import clearaudio.synthesizer.filters.WarmifyFilter;
import clearaudio.synthesizer.sources.Guitar;
import clearaudio.synthesizer.sources.ShepardRissetGlissando;
import clearaudio.synthesizer.sources.Sinusoid;

import org.junit.Test;

public class SynthesizerDemo
{

  @Test
  public void demoSinusoid() throws LineUnavailableException
  {
    Sinusoid lSinusoid = new Sinusoid();

    SoundOut lSoundOut = new SoundOut();

    Synthesizer lSynthesizer = new Synthesizer(lSinusoid, lSoundOut);

    lSoundOut.start();
    for (int i = 0; i < 1000; i++)
    {
      lSinusoid.setFrequencyInHertz(440 + 10 * i);
      lSynthesizer.playSamples();
    }
    lSoundOut.stop();
  }

  @Test
  public void demoHighlyFilteredGuitar() throws LineUnavailableException
  {
    Guitar lGuitar = new Guitar();

    NoiseFilter lNoiseFilter = new NoiseFilter();
    lNoiseFilter.setSource(lGuitar);

    WarmifyFilter lWarmifyFilter = new WarmifyFilter(1f);
    lWarmifyFilter.setSource(lNoiseFilter);

    ReverbFilter lReverbFilter = new ReverbFilter();
    lReverbFilter.setSource(lWarmifyFilter);/**/

    LowPassFilter lLowPassFilter = new LowPassFilter();
    lLowPassFilter.setSource(lReverbFilter);/**/

    lGuitar.setAmplitude(0.5f);

    SoundOut lSoundOut = new SoundOut();

    Synthesizer lSynthesizer = new Synthesizer(lLowPassFilter,
                                               lSoundOut);

    lSoundOut.start();
    for (int i = 0; i < 100000; i++)
    {
      lSynthesizer.playSamples();

      lGuitar.setFrequencyInHertz((float) (220 + 440
                                           + 440 * sin(2 * PI
                                                       * i
                                                       / 1000)));
      if (i % 10 == 0)
        lGuitar.strike(0.5f);

    }

    lSoundOut.stop();
  }

  @Test
  public void demoShepardRissetGlissando() throws LineUnavailableException
  {
    ShepardRissetGlissando lShepardRissetGlissando =
                                                   new ShepardRissetGlissando();
    lShepardRissetGlissando.setAmplitude(0.05f);

    SoundOut lSoundOut = new SoundOut();

    Synthesizer lSynthesizer =
                             new Synthesizer(lShepardRissetGlissando,
                                             lSoundOut);

    lSoundOut.start();
    for (int i = 0; i < 10000; i++)
    {
      lSynthesizer.playSamples();
      lShepardRissetGlissando.changeVirtualFrequency(+1f);
    }
    lSoundOut.stop();
  }

}
