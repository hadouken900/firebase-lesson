package com.example.randomname;  // поменять название пакета на свой

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //поле ввода нового сообщения
    private EditText messageContent;
    private RecyclerView messagesRecyclerView;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //привязка по id
        messagesRecyclerView = findViewById(R.id.messageList);
        messageContent = findViewById(R.id.editTextMessage);

        //создание адаптера и подключение его к recycleView
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //добавление itemTouchHelper для удаления элемента списка свайпом вправо
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                messageList.remove(viewHolder.getAdapterPosition());
                messageAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(messagesRecyclerView);
    }

    //onClick
    public void sendMessage(View view) {
        Message mess = new Message(messageContent.getText().toString());
        messageContent.setText("");
        messageList.add(mess);
        messageAdapter.notifyDataSetChanged();

    }
}