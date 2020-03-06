import * as functions from 'firebase-functions'
import * as admin from 'firebase-admin'
admin.initializeApp(functions.config().firebase)

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

export const firstSignUp = functions.auth.user().onCreate((user)=> {

	// Set the last active date to yesterday
	var lastActiveDate = new Date();
	lastActiveDate.setHours(0, 0, 0, 0);
	lastActiveDate = new Date(lastActiveDate.getTime() - 1000 * 3600 * 24);

	var userObject = {
		uid: user.uid,
//      Empty until is received from the db.
//		This is a workaround since the display name isn't created when the user is created.
		name: ' ',
		weeklyExperience: 0,
		email: user.email,
		league: 0,
		weeklyGroup: 0,
		streak: 0,
		money: 0,
		lastActiveDate: lastActiveDate,
		hasStreakFreeze: false,
		hasWager: false,
		wagerStreak: 0
	};

//  TODO: Get these valeus from the db thresholds.
    var userCollections = {
        achievements: [
			{
				id: '0',
				stars: 0,
				exp: 0,
                currentStarProgress: 0,
                currentStarMaxProgress: 10
			},
			{
				id: '1',
				stars: 0,
				exp: 0,
                currentStarProgress: 0,
                currentStarMaxProgress: 150
			}
		]
    };

    admin.database().ref('users/' + user.uid).set(userObject);
    admin.database().ref('user_collections/' + user.uid).set(userCollections);
});

export const onLogIn = functions.https.onCall((data, context) => {
    var uid = context.auth!.uid;
    let ref = admin.database().ref('users/' + uid);
                ref.once('value', (snapshot) => {
                    var oldLastActiveDate = new Date(snapshot.val().lastActiveDate);
                    oldLastActiveDate.setHours(0, 0, 0, 0);

                    // Check the days difference to update the streak accordingly.
                    var streak = snapshot.val().streak;
                    var newLastActiveDate = new Date();
                    newLastActiveDate.setHours(0, 0, 0, 0);
                    var differenceInTime = newLastActiveDate.getTime() - oldLastActiveDate.getTime();
                    var differenceInDays = differenceInTime / (1000 * 3600 * 24);

                    // TODO: Delete logs.
                    console.log("ERROR: The old data is " + snapshot.val());
                    console.log("ERROR: The numeric old date is:" + snapshot.val().lastActiveDate);
                    console.log("ERROR: The old date is:" + oldLastActiveDate.getTime());
                    console.log("ERROR: The new date is:" + newLastActiveDate.getTime());
                    console.log("ERROR: User id: " + "The difference in days is: " + differenceInDays);

                    if (differenceInDays > 1) {
                        streak = 0;
                        newLastActiveDate = new Date(newLastActiveDate.getTime() - 3600 * 1000 * 24);
                    } else {
                        newLastActiveDate = oldLastActiveDate;
                    }

                    ref.update({
                            "streak": streak,
                            "lastActiveDate": newLastActiveDate.getTime()
                        }, function(error) {
                            if (error) {
                                console.log("ERROR: User id: " + uid);
                            } else {
                                console.log("SUCCESS: User id: " + uid);
                            }
                    });
                });
});

export const onAccountUpdate = functions.database
    .ref('users/{userId}')
    .onUpdate((change, context) => {
    const oldMoney = change.before.child('money').val();
    const newMoney = change.after.child('money').val();

    // TODO: Delete logs
    console.log('The old money is: ' + oldMoney);
    console.log('The new money is: ' + newMoney);

    // If the amount of money was just increased.
    // Get the current achievement data
    if (newMoney > oldMoney) {
        let achRef = admin.database().ref('user_collections/' + context.params.userId + '/achievements/1');
        achRef.once('value', (achSnapshot) => {
            var currentStarProgress = achSnapshot.val().currentStarProgress;
            var currentStarMaxProgress = achSnapshot.val().currentStarMaxProgress;

            console.log('The current star progress is: ' + currentStarProgress);
            console.log('The current star max progress is: ' + currentStarMaxProgress);

            // If the new amount of money is bigger than the current achievement progress
            // Update it
            if (newMoney > currentStarProgress) {
                currentStarProgress = newMoney;

                // Update the achievement's progress.
                achRef.update({
                    "currentStarProgress": currentStarProgress
                }, function(error) {
                    if (error) {
                        console.log("ERROR: User id: " + context.params.userId);
                    } else {
                        console.log("SUCCESS: User id: " + context.params.userId);
                    }
                });
            }

            // If the current progress is bigger or equal than the max progress
            // Get the next threshold's data
            if (currentStarProgress >= currentStarMaxProgress) {
                let nextThresholdRef = admin.database().ref('achievementThresholds/1')
                    .orderByChild('star')
                        .equalTo(achSnapshot.val().stars + 1);
                nextThresholdRef.on('child_added', (nextThresholdSnapshot) => {
                    var nextThresholdMaxProgress = nextThresholdSnapshot!.val().maxProgress;
                    var nextThresholdStar = nextThresholdSnapshot!.val().star;

                    console.log('The next threshold max progress is: ' + nextThresholdMaxProgress);
                    console.log('The next threshold star is: ' + nextThresholdStar);

                    // Update the current achievement's star and max progress according
                    // to the next threshold.
                    achRef.update({
                        "currentStarMaxProgress": nextThresholdMaxProgress,
                        "stars": nextThresholdStar
                        }, function(error) {
                            if (error) {
                                console.log("ERROR: User id: " + context.params.userId);
                            } else {
                                console.log("SUCCESS: User id: " + context.params.userId);
                            }
                        });
                });
            }
        });
    }
});

export const onPurchaseStreakFreeze = functions.database
    .ref('users/{userId}/hasStreakFreeze')
    .onUpdate((change, context) => {
        // Retrieve the user's data from the db.
        let ref = admin.database().ref('users/' + context.params.userId);
        ref.once('value', (snapshot) => {
            var money = snapshot.val().money;
            var oldHasStreakFreeze = change.before.val();
            var newHasStreakFreeze = change.after.val();
            console.log("money: " + money);

            // If the user has enough money, make the purchase.
            if (money >= 50 && !oldHasStreakFreeze && newHasStreakFreeze) { // TODO: Get the price from the database.
                ref.update({
                    "hasStreakFreeze": true,
                    "money": money - 50 // TODO: Get the price from the database.
                }, function (error) {
                    if (error) {
                        console.log("ERROR: couldn't update user: " + context.params.userId);
                    } else {
                        console.log("SUCCESS: User: " + context.params.userId + " purchased wager");
                    }
                });
            }
        });
    });

export const onPurchaseWager = functions.database
    .ref('users/{userId}/hasWager')
    .onUpdate((change, context) => {

        // Retrieve the user's data from the db.
        let ref = admin.database().ref('users/' + context.params.userId);
        ref.once('value', (snapshot) => {
            var money = snapshot.val().money;
            var oldHasWager = change.before.val();
            var newHasWager = change.after.val();
            console.log("money: " + money);

            // If the user has enough money, make the purchase.
            if (money >= 50 && !oldHasWager && newHasWager) { // TODO: Get the price from the database.
                ref.update({
                    "hasWager": true,
                    "money": money - 50, // TODO: Get the price from the database.
                    "wagerStreak": 0
                }, function (error) {
                    if (error) {
                        console.log("ERROR: couldn't update user: " + context.params.userId);
                    } else {
                        console.log("SUCCESS: User: " + context.params.userId + " purchased wager");
                    }
                });
            }
        });
    });

export const onTaskFinish = functions.database
    .ref('user_collections/{userId}/tasks/{taskId}')
    .onUpdate((change, context) => {

        // Get the new and the old values.
        const newVal: boolean = Boolean(change.after.child('is_done').val());
        const oldVal: boolean = Boolean(change.before.child('is_done').val());

        // Log to the entry to this function.
        console.log("INFO: onTaskFinish is called by Tom " + context.params.userId);

        // Check if the value was changed.
        if (oldVal == false && newVal == true) {
            let ref = admin.database().ref('users/' + context.params.userId);
            ref.once('value', (snapshot) => {
                var oldExp = snapshot.val().weeklyExperience;
                var oldLastActiveDate = new Date(snapshot.val().lastActiveDate);
                oldLastActiveDate.setHours(0, 0, 0, 0);

                // Check the days difference to update the streak accordingly.
                var streak = snapshot.val().streak;
                var wagerStreak = snapshot.val().wagerStreak;
                var hasWager = snapshot.val().hasWager;
                var hasStreakFreeze = snapshot.val().hasStreakFreeze;
                var newLastActiveDate = new Date();
                newLastActiveDate.setHours(0, 0, 0, 0);
                var differenceInTime = newLastActiveDate.getTime() - oldLastActiveDate.getTime();
                var differenceInDays = differenceInTime / (1000 * 3600 * 24);

                // TODO: Delete logs.
                console.log("ERROR: The old data is " + snapshot.val());
                console.log("ERROR: The numeric old date is:" + snapshot.val().lastActiveDate);
                console.log("ERROR: The old date is:" + oldLastActiveDate.getTime());
                console.log("ERROR: The new date is:" + newLastActiveDate.getTime());
                console.log("ERROR: User id: " + "The difference in days is: " + differenceInDays);

                if (differenceInDays == 1 || (hasStreakFreeze && differenceInDays == 2)) { // TODO: add "or 2 days and a streak freeze(and obviously set the hasStreakFreeze field to false"
                    streak = streak + 1;

                    // TODO: Also update the wager streak.
                    if (hasWager) {
                        wagerStreak += 1;
                    }

                    if (hasStreakFreeze && differenceInDays == 2) {
                        hasStreakFreeze = false;
                    }

                    let achRef = admin.database().ref('user_collections/' + context.params.userId + '/achievements/0');
                    achRef.once('value', (achSnapshot) => {

                        // Get the current progress and stars
                        var curProgress = achSnapshot.val().currentStarProgress;
                        var curStars = achSnapshot.val().stars

                        console.log("The current progress is: " + curProgress);
                        console.log("The current amount of stars is: " + curStars);

                        // If the current streak is bigger than the one in the achievement
                        // set the current streak as the one in the achievement.
                        if (streak > curProgress) {
                            curProgress = streak;

                            // Get the current level achievement threshold data.
                            let thresholdRef = admin.database().ref('achievementThresholds/0')
                                .orderByChild('star')
                                .equalTo(curStars);
                            thresholdRef.on('child_added', (thresholdSnapshot) => {
                                var curThresholdMaxProgress = thresholdSnapshot!.val().maxProgress;
                                console.log("The current threshold max progress is: " + curThresholdMaxProgress);

                                // If the current progress is bigger than the current
                                // star's threshold, get the next star's threshold.
                                if (curProgress >= curThresholdMaxProgress) {
                                    let nextThresholdRef = admin.database().ref('achievementThresholds/0')
                                        .orderByChild('star')
                                        .equalTo(achSnapshot.val().stars + 1);
                                    nextThresholdRef.on('child_added', (nextThresholdSnapshot) => {
                                        var nextMaxProgress = nextThresholdSnapshot!.val().maxProgress;
                                        var nextStar = nextThresholdSnapshot!.val().star;

                                        // TODO" Delete logs
                                        console.log("The next threshold max progress is: " + nextMaxProgress);
                                        console.log("The next threshold max progress is: " + nextMaxProgress);

                                        // Update the current achievement's star and max progress according
                                        // to the next threshold.
                                        achRef.update({
                                             "currentStarMaxProgress": nextMaxProgress,
                                             "stars": nextStar
                                             }, function(error) {
                                                if (error) {
                                                    console.log("ERROR: User id: " + context.params.userId);
                                                } else {
                                                    console.log("SUCCESS: User id: " + context.params.userId);
                                                }
                                        });
                                    });
                                }
                            });

                            // Update the current progress.
                            achRef.update({
                                "currentStarProgress": curProgress
                                }, function(error) {
                                    if (error) {
                                        console.log("ERROR: User id: " + context.params.userId);
                                    } else {
                                        console.log("SUCCESS: User id: " + context.params.userId);
                                    }
                            });
                        }
                     });
                } else {
                    hasStreakFreeze = false;
                    hasWager = false;
                    wagerStreak = 0;
                }

                ref.update({
                        "weeklyExperience": oldExp + 10,
                        "lastActiveDate": newLastActiveDate.getTime(),
                        "streak": streak,
                        "hasStreakFreeze": hasStreakFreeze,
                        "hasWager": hasWager,
                        "wagerStreak": wagerStreak
                    }, function(error) {
                        if (error) {
                            console.log("ERROR: User id: " + context.params.userId);
                        } else {
                            console.log("SUCCESS: User id: " + context.params.userId);
                        }
                });
            });
        }
    });

//export const weekylRest = functions.pubsub.schedule('every week')
//	.timeZone('America/New_York')
//	.onRun((context) => {
//
//		// Reset the weekly experience.
//		const db = admin.database();
//		const ref = db.ref('users');
//
//		ref.once("value").then((snapshot) => {
//			var updates:any = {};
//
//			snapshot.forEach((userSnapshot) => {
//				var newWeeklyExperience = 5;
//				updates[userSnapshot.key + "/weeklyExperience"] = newWeeklyExperience;
//			});
//
//			ref.update(updates);
//		});
//
//
//		return null;
//	});
