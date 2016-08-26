package github.yaa110.memento.fragment.template;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.yaa110.memento.R;
import github.yaa110.memento.activity.NoteActivity;
import github.yaa110.memento.db.OpenHelper;
import github.yaa110.memento.model.DatabaseModel;
import github.yaa110.memento.model.Note;

abstract public class NoteFragment extends Fragment {
	public Note note = null;
	public Callbacks activity;
	private View deleteBtn;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(getLayout(), container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		deleteBtn = getActivity().findViewById(R.id.delete_btn);

		Intent data = getActivity().getIntent();
		long noteId = data.getLongExtra(OpenHelper.COLUMN_ID, DatabaseModel.NEW_MODEL_ID);
		final long categoryId = data.getLongExtra(OpenHelper.COLUMN_PARENT_ID, DatabaseModel.NEW_MODEL_ID);

		if (noteId != DatabaseModel.NEW_MODEL_ID) {
			note = Note.find(noteId);
		}

		if (note == null) {
			note = new Note();
			activity.setNoteResult(NoteActivity.RESULT_NEW, false);
			deleteBtn.setVisibility(View.GONE);
			note.categoryId = categoryId;
			note.type = data.getIntExtra(OpenHelper.COLUMN_TYPE, DatabaseModel.TYPE_NOTE_SIMPLE);
		} else {
			activity.setNoteResult(NoteActivity.RESULT_EDIT, false);
			deleteBtn.setVisibility(View.VISIBLE);
			deleteBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					activity.setNoteResult(NoteActivity.RESULT_DELETE, true);
				}
			});
		}

		init(view);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		activity = (Callbacks) context;
	}

	abstract public int getLayout();
	abstract public void saveNote();
	abstract public void init(View view);

	public interface Callbacks {
		void setNoteResult(int result, boolean closeActivity);
	}
}