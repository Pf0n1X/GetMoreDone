import* as functions from 'firebase-functions'
import * as admin from 'firebase-admin'
admin.initializeApp(functions.config().firebase)

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

export const firstSignUp = functions.auth.user().onCreate((user)=> {
	// const userData = user.data;
	

	var userObject = {
		uid: user.uid,
//      Empty until is received from the db.
//		This is a workarorund since the display name isn't created when the user is created.
		name: ' ',
		weeklyExperience: 0,
		email: user.email,
		league: 0,
		weeklyGroup: 0,
		streak: 0,
		money: 0
	};

    var userCollections = {
        Achievements: [
			{
				id: 1,
				stars: 0,
				exp: 0
			},
			{
				id: 2,
				stars: 0,
				exp: 0
			},
			{
				id: 3,
				stars: 0,
				exp: 0
			}
		]
    };

    admin.database().ref('users/' + user.uid).set(userObject);
    admin.database().ref('user_collections/' + user.uid).set(userCollections);
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
