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
		lastActiveDate: lastActiveDate
	};

    var userCollections = {
        achievements: [
			{
				id: '1',
				stars: 0,
				exp: 0,
                currentStarProgress: 0,
                currentStarMaxProgress: 10
			},
			{
				id: '2',
				stars: 0,
				exp: 0,
                currentStarProgress: 0,
                currentStarMaxProgress: 10
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

                if (differenceInDays == 1) {
                    streak = streak + 1;
                }

                ref.update({
                        "weeklyExperience": oldExp + 10,
                        "lastActiveDate": newLastActiveDate.getTime(),
                        "streak": streak
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
