from flask import Flask, request, jsonify
from flask_restful import Api, Resource, reqparse
import pickle
import pandas as pd
import firebase_admin
from firebase_admin import credentials, firestore
import sklearn

app = Flask(__name__)
api = Api(app)

cred = credentials.Certificate("./credentials/firebase_creds.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

user_rec_args = reqparse.RequestParser()
user_rec_args.add_argument("user-uid", type=str, required=True)

sim_rec_args = reqparse.RequestParser()
sim_rec_args.add_argument("item-name", type=str, required=True)

model = pickle.load(open("./model/food_recommendation.pkl", "rb"))

df = pd.read_csv("./dataset/food_recommendation.csv")
names = list(df["name"].copy())
features = df.drop(["name"], axis=1)


class UserRecommendation(Resource):
    def get(self):
        args = user_rec_args.parse_args()
        user_id = args["user-uid"]

        doc_ref = db.collection("Users").document(user_id)
        doc = doc_ref.get()

        if doc.exists:
            doc = doc.to_dict()

            user_feature = doc["user_choice"]
            user_feature.insert(0, doc["spice"])
            user_feature.insert(0, doc["preference"])

            rec = list()
            dist, ind = model.kneighbors([user_feature], n_neighbors=11)
            for i in ind[0][1:]:
                rec.append(names[i])

            resp = jsonify({"recommendation": rec})
            resp.status_code = 201
            return resp

        else:
            resp = jsonify({"message": "user not found"})
            resp.status_code = 400
            return resp


class SimilarRecommendation(Resource):
    def get(self):
        try:
            args = sim_rec_args.parse_args()
            item_name = args["item-name"]

            if item_name in names:
                food_index = names.index(item_name)

                user_request = features.iloc[food_index]
                rec = list()
                dist, ind = model.kneighbors([user_request], n_neighbors=6)
                for i in ind[0][1:]:
                    rec.append(names[i])

                resp = jsonify({"recommendation": rec})
                resp.status_code = 201
                return resp

            else:
                resp = jsonify({"message": "item not found"})
                resp.status_code = 400
                return resp

        except Exception as e:
            resp = jsonify({"message": e})
            resp.status_code = 400
            return resp


api.add_resource(UserRecommendation, "/user-recommendation")
api.add_resource(SimilarRecommendation, "/similar-recommendation")

if __name__ == "__main__":
    app.run(debug=False)

