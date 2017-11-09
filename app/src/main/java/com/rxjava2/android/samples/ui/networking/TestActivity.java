package com.rxjava2.android.samples.ui.networking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.utils.Utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = (TextView) findViewById(R.id.tv);
        merge();
        concatenate();
    }


    public void merge() {
        final String[] message = {""};

        Observable<Character> observable1 = Observable.just('m', 'r', 'g', 'd');
        Observable<Character> observable2 = Observable.just('e', 'e');

        getAllMyFriendsObservable().merge(observable1, observable2).subscribe(new Observer<Character>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull Character character) {
                message[0] = message[0] + character;
                textView.setText(message[0]);
                Log.w(TAG, "onNext: " + character);

            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void concatenate() {
        final String[] count = new String[1];
        Observable<Integer> seq1 = Observable.range(0, 10);
        Observable<Integer> seq2 = Observable.range(11, 20);

        Observable.concat(seq1, seq2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.e(TAG, "Concatenate:");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        textView.setText(textView.getText() + " " + integer);
                        Log.w(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void flatMapAndFilter(View view) {
        getAllMyFriendsObservable()
                .flatMap(new Function<List<User>, ObservableSource<User>>() { // flatMap - to return users one by one
                    @Override
                    public ObservableSource<User> apply(List<User> usersList) throws Exception {
                        return Observable.fromIterable(usersList); // returning user one by one from usersList.
                    }
                })
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        // filtering user who follows me.
                        return user.isFollowing;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        // only the user who is following me comes here one by one
                        Log.d(TAG, "onNext:");
                        Log.d(TAG, "user : " + user.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logError(TAG, e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private Observable<List<User>> getAllMyFriendsObservable() {
        return Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllFriends/{userId}")
                .addPathParameter("userId", "1")
                .build()
                .getObjectListObservable(User.class);
    }
}
