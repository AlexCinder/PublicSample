package com.example.notesapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
;import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

    private EditText edTitle;
    private EditText edDescription;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private AddNoteViewModel viewModel;
    private ImageView im;
    private ConstraintLayout imageCL;
    private FloatingActionButton addImage;
    private ImageButton editImage;
    private ImageButton deleteImage;
    private String uri = "empty";
    private boolean state = true;
    private Note note;
    private final String pattern = "dd-MMM-yyyy-HH:mm";
    private FirebaseFirestore db;


    private final ActivityResultLauncher<Intent> searchImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData().toString();

                        im.setImageURI(result.getData().getData());
                        getContentResolver().takePersistableUriPermission(result.getData().
                                getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        viewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);
        edTitle = findViewById(R.id.editTextTitle);
        edDescription = findViewById(R.id.editTextTextMultiLine);
        spinner = findViewById(R.id.spinner);
        radioGroup = findViewById(R.id.rgPriority);
        imageCL = findViewById(R.id.imageCL);
        addImage = findViewById(R.id.addImage);
        editImage = findViewById(R.id.editButton);
        deleteImage = findViewById(R.id.deleteButton);
        im = findViewById(R.id.im);
        getIntentForEdit();

    }

    public static final String EXTRA_INTENT = "intent";

    private void getIntentForEdit() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_INTENT) && intent.hasExtra("state")) {
            note = (Note) intent.getSerializableExtra(EXTRA_INTENT);
            state = intent.getBooleanExtra("state", true);
            if (note != null && !state) {
                edTitle.setText(note.getTitle());
                edDescription.setText(note.getDescription());
                spinner.setSelection(getSpinnerIndex(spinner, note.getDayOfWeek()));
                ((RadioButton) radioGroup.
                        getChildAt(getRadioButtonTag(radioGroup, note.getPriority()))).
                        setChecked(true);
                if (!note.getUri().equals("empty") && !(note.getUri() == null)) {
                    uri = note.getUri();
                    imageCL.setVisibility(View.VISIBLE);
                    im.setImageURI(Uri.parse(note.getUri()));
                }
            }
        }
    }

    public void done(View view) {
        String lastEdit = convertTime();
        String description = edDescription.getText().toString().trim();
        String title = edTitle.getText().toString().trim();
        int priority = Integer.parseInt(findViewById(radioGroup.getCheckedRadioButtonId()).
                getTag().toString().trim());
        String dayOfWeek = spinner.getSelectedItem().toString().trim();
        if (!title.isEmpty() || !description.isEmpty()) {
            if (state) {
                Note tempNote = new Note(title, description, dayOfWeek, priority, uri, lastEdit);
                viewModel.insertNoteTask(tempNote);

            } else {
                note.setUri(uri);
                note.setDescription(description);
                note.setTitle(title);
                note.setPriority(priority);
                note.setDayOfWeek(dayOfWeek);
                note.setLastEditDate(lastEdit);
                viewModel.upNoteTask(note);

            }
            this.finish();
        }
    }

    public void editImage(View view) {
        switch (view.getId()) {
            case (R.id.addImage):
                imageCL.setVisibility(View.VISIBLE);
                addImage.setVisibility(View.GONE);
                break;
            case (R.id.deleteButton):
                im.setImageResource(0);
                uri = "empty";
                imageCL.setVisibility(View.GONE);
                addImage.setVisibility(View.VISIBLE);
                break;
            case (R.id.editButton):
                Intent chooseImage = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseImage.setType("image/*");
                searchImage.launch(chooseImage);
                break;


        }


    }

    private int getSpinnerIndex(Spinner spinner, String value) {

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;

    }

    private int getRadioButtonTag(RadioGroup radioGroup, int tag) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (Integer.parseInt(radioGroup.getChildAt(i).
                    getTag().toString().trim()) == tag)
                return i;
        }
        return 0;
    }

    public String convertTime() {
        SimpleDateFormat nowTime = new SimpleDateFormat(pattern, Locale.getDefault());
        return nowTime.format(new Date());
    }
}