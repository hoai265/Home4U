package app.hoai.bkit4u.home4u;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.listener.SendActionListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.DeviceActionCollection;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;
import app.hoai.bkit4u.home4u.model.HomeItemCollection;
import app.hoai.bkit4u.home4u.model.HomeItemModel;
import app.hoai.bkit4u.home4u.model.SpeechControlModel;
import app.hoai.bkit4u.home4u.model.SpeechModelCollection;
import app.hoai.bkit4u.home4u.model.type.DeviceType;
import app.hoai.bkit4u.home4u.model.type.SpeechType;

public class SpeechControlActivity extends AppCompatActivity
{
    View mMicView;
    TextView mTextSpeech;
    ProgressBar mProgressBar;
    View mProgresscontainer;
    SpeechModelCollection mCollection;
    ImageView mImageOk;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgresscontainer = findViewById(R.id.progress_container);
        mMicView = findViewById(R.id.ic_mic);
        mTextSpeech = (TextView) findViewById(R.id.textSpeech);
        mTextSpeech.setVisibility(View.GONE);
        mImageOk = (ImageView) findViewById(R.id.image_ok);
        mMicView.setVisibility(View.GONE);
        mMicView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                promptSpeechInput();
            }
        });

        mCollection = new SpeechModelCollection();

        fetchData();
    }


    private void promptSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try
        {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a)
        {
            Log.d("Home4U", "Do not support speech!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
            {
                if (resultCode == RESULT_OK && null != data)
                {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mTextSpeech.setVisibility(View.VISIBLE);
                    mTextSpeech.setText(result.get(0));
                    onFindingAction();
                    SpeechControlModel model = mCollection.findModelByDescribe(result.get(0).toLowerCase());
                    if (null != model)
                    {
                        mTextSpeech.setText(result.get(0));
                        mImageOk.setImageResource(R.drawable.ic_check_circle_black_48dp);
                        mImageOk.setVisibility(View.VISIBLE);
                        NetworkController.getInstance().excuteSpeechControl(new SendActionListener()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Snackbar.make(mMicView, "Successfully!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void onError()
                            {
                                Snackbar.make(mMicView, "Error!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        },model);
                        onFetchcompleted();
                    }
                    else
                    {
                        mTextSpeech.setText(result.get(0));
                        mImageOk.setImageResource(R.drawable.ic_cancel_black_48dp);
                        mImageOk.setVisibility(View.VISIBLE);
                        onFetchcompleted();
                    }

                }
                break;
            }

        }
    }

    public void fetchData()
    {
        onFetchEvent();
    }

    void onFetchEvent()
    {
        NetworkController.getInstance().getEventCollection(new ResponseListener<HomeItemCollection>()
        {
            @Override
            public void onResponse(HomeItemCollection response)
            {
                ArrayList<HomeItemModel> list = response.getModels();
                for (HomeItemModel model : list)
                {
                    mCollection.addModel(new SpeechControlModel(model.getTypeName().toLowerCase(), model.getId(), SpeechType.EVENT));
                    Log.d("Home4U - Describe", model.getTypeName());
                }

                onFetchAction();
            }

            @Override
            public void onError()
            {
                onFetchcompleted();
            }
        });
    }

    void onFetchAction()
    {
        NetworkController.getInstance().getAllAction(new ResponseListener<DeviceActionCollection>()
        {
            @Override
            public void onResponse(DeviceActionCollection response)
            {
                ArrayList<DeviceActionModel> list = response.getItems();

                for (DeviceActionModel model : list)
                {
                    BaseDeviceModel device = model.getDevice();
                    String describe = "";
                    if(device.getType() == DeviceType.IR)
                    {
                        describe = model.getName();
                    } else
                    {
                        describe = device.getName() + " " + model.getName();
                    }

                    Log.d("Home4U - Describe", describe);
                    SpeechControlModel speechModel = new SpeechControlModel(describe.toLowerCase(), model.getId(), SpeechType.ACTION);
                    speechModel.setActionModel(model);
                    mCollection.addModel(speechModel);
                }

                onFetchcompleted();
            }

            @Override
            public void onError()
            {
                onFetchcompleted();
            }
        });
    }

    void onFetchcompleted()
    {
        mProgresscontainer.setVisibility(View.GONE);
        mMicView.setVisibility(View.VISIBLE);
    }

    void onFindingAction()
    {
        mProgresscontainer.setVisibility(View.VISIBLE);
        mMicView.setVisibility(View.VISIBLE);
    }
}
