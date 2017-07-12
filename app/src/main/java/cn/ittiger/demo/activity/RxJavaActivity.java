package cn.ittiger.demo.activity;

import cn.ittiger.demo.bean.User;
import cn.ittiger.demo.util.UIUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baina on 16-9-30.
 */
public class RxJavaActivity extends ListActivity {

    @Override
    public List<String> getData() {

        List<String> list = new ArrayList<>(10);
        list.add("最基本方式创建Observable和Subscriber");
        list.add("简化创建方式");
        list.add("简单事件变换map");
        list.add("接收集合事件");
        list.add("复杂变换flatmap");
        list.add("事件数据过滤filter");
        list.add("切换到子线程执行");
        list.add("切换到UI线程");
        return list;
    }

    @Override
    public void onItemClick(int position, View itemView) {

        switch (position) {
            case 0://最基本方式创建Observable和Subscriber
                observableSubscribe();
                break;
            case 1://简化创建方式
                simpleObservableSubscriber();
                break;
            case 2://简单事件变换map
                simpleChangeEvent();
                break;
            case 3://接收集合事件
                observableFrom();
                break;
            case 4://复杂变换flatmap
                flatmapSmaple();
                break;
            case 5://事件数据过滤filter
                filterEvent();
                break;
            case 6://切换到子线程执行
                newTheadRun();
                break;
            case 7://切换到UI线程执行
                uiThreadRun();
                break;
        }
    }

    void observableSubscribe() {

        //最原始的方式创建Observable
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("Observable subscribe Subscriber");
//                subscriber.onCompleted();
            }
        });

        //最原始的方式创建Subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

                UIUtil.showToast(mContext, "订阅执行结束");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

                UIUtil.showToast(mContext, s);
            }
        };
        observable.subscribe(subscriber);
    }

    void simpleObservableSubscriber() {

        //创建一个只发出一个事件就结束的对象
        Observable<String> observable = Observable.just("简化方式创建Observable");

        //创建一个只关心onNext处理的subscriber
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {

                UIUtil.showToast(mContext, s);
            }
        };
        //此方法有重载版本，可以传递处理onError,onComplete的Action
        observable.subscribe(onNextAction);

        /*
        //链式写法
        Observable.just("简化方式创建Observable")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        UIUtil.showToast(mContext, s);
                    }
                });*/
    }

    void simpleChangeEvent() {

        Observable.just("初始事件")
            //map操作是将一个事件变换为另外一个事件，只会影响到当前的subscriber
            //此处将一个String转换成了一个Integer，所以类型是可用改变的
            .map(new Func1<String, Integer>() {
                @Override
                public Integer call(String s) {

                    return s.hashCode();
                }
            })
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer s) {

                    UIUtil.showToast(mContext, s.toString());
                }
            });
    }


    private void observableFrom() {

        String[] array = {"数据1", "数据2", "数据3"};
        Observable.from(array)
            .subscribe(new Action1<String>() {
                @Override
                public void call(String s) {

                    UIUtil.showToast(mContext, s);
                }
            });
    }

    void flatmapSmaple() {

        String[] array = {"张三", "李四", "王麻子", "赵六"};
        Observable.from(array)
            //将一个事件数据变换为另一种事件输出的Observable
            .flatMap(new Func1<String, Observable<User>>() {
                @Override
                public Observable<User> call(String s) {

                    return Observable.just(new User(s, 10));
                }
            })
            .subscribe(new Action1<User>() {
                @Override
                public void call(User user) {

                    UIUtil.showToast(mContext, user.getName());
                }
            });
    }

    void filterEvent() {

        String[] array = {"张三", "李四", "王麻子", "赵六"};
        Observable.from(array)
                //将一个事件数据变换为另一种事件输出的Observable
                .flatMap(new Func1<String, Observable<User>>() {
                    @Override
                    public Observable<User> call(String s) {

                        int age = 10;
                        if(s.length() > 2) {
                            age = 0;
                        }
                        return Observable.just(new User(s, age));
                    }
                })
                //将age <= 0的事件过滤掉
                .filter(new Func1<User, Boolean>() {
                    @Override
                    public Boolean call(User user) {

                        return user.getAge() > 0;
                    }
                })
                //只取符合条件的前两个结果
                .take(2)
                //在subscribe执行之前做些额外的操作，例如将得到的数据保存到磁盘上
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        System.out.print(user.toString());
                    }
                })
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {

                        UIUtil.showToast(mContext, user.getName());
                    }
                });
    }

    void newTheadRun() {

        UIUtil.showToast(mContext, Thread.currentThread().toString());
        Observable.just("new thread run")
                .subscribeOn(Schedulers.io())//在子线程中执行操作
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {

                        String toast = Thread.currentThread().toString() + "," + s;
                        //此时会出现异常，无法执行UI操作
                        UIUtil.showToast(mContext, toast);
                    }
                });
    }

    void uiThreadRun() {

        UIUtil.showToast(mContext, Thread.currentThread().toString());
        Observable.just("new thread run")
                .subscribeOn(Schedulers.io())//在子线程中执行操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程中更新结果
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {

                        String toast = Thread.currentThread().toString() + "," + s;
                        UIUtil.showToast(mContext, toast);
                    }
                });
    }
}
