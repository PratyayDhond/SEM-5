#include<bits/stdc++.h>

#include<ctime>

using namespace std;

void transmission(long long int & i, long long int & windowSize, long long int & totalFrames, long long int & tt) {
  while (i <= totalFrames) {
    int z = 0;
    for (int k = i; k < i + windowSize && k <= totalFrames; k++) {
      cout << "Sending Frame " << k << "..." << endl;
      for(int i = 0; i < 99999; i++)
        for(int j = 0; j < 1500; j++);
      tt++;
    }



    for (int k = i; k < i + windowSize && k <= totalFrames; k++) {
      int f = rand() % 2;
      if (!f) {
        cout << "Acknowledgment for Frame " << k << "..." << endl;
        z++;
      } else {
        cout << "Timeout!! Frame Number : " << k << " Not Received" << endl;
        cout << "Retransmitting Window..." << endl;
        for(int i = 0; i < 64000; i++){
            for(int j =0; j < 10000; j++);
        }
        break;
      }
    }
    cout << "\n";
    i = i + z;
  }
}

int main() {
  long long int totalFrames, windowSize, tt = 0;
  srand(time(NULL));
  cout << "Enter the Total number of frames : ";
  cin >> totalFrames;
  cout << "Enter the Window Size : ";
  cin >> windowSize;
  long long int i = 1;
  transmission(i, windowSize, totalFrames, tt);
  cout << "Total number of frames which were sent and resent are : " << tt << "," << tt - totalFrames <<  endl;
  return 0;
}