#include<bits/stdc++.h>

#include<ctime>

using namespace std;

void transmission(long long int & i, long long int & windowSize, long long int & totalFrames, long long int & tt) {
  vector<int> receiveWindow(windowSize+1,0);
  long long int smallestUnacknowledgedFrame = 0;
  while (smallestUnacknowledgedFrame < totalFrames) {
    int z = 0;
    for (int k = smallestUnacknowledgedFrame; k < smallestUnacknowledgedFrame + windowSize && k < totalFrames; k++) {
      if(receiveWindow[k] == 0){
          cout << "Sending Frame " << k + 1 << "..." << endl;
          for(int i = 0; i < 99999; i++)
            for(int j = 0; j < 1500; j++);
          tt++;
      }
    }
    for (int k = smallestUnacknowledgedFrame; k < smallestUnacknowledgedFrame + windowSize && k < totalFrames; k++) {
      int f = rand() % 2;
      if (!f) {
        cout << "Acknowledgment for Frame " << k + 1 << "..." << endl;
        receiveWindow[k] = 1;
      } else {
        if(receiveWindow[k] == 0){
            cout << "Timeout!! Frame Number : " << k + 1 << " Not Received" << endl;
            cout << "Retransmitting Window..." << endl;
            for(int i = 0; i < 64000; i++){
                for(int j =0; j < 10000; j++);
            }
        }
      }
    }
    cout << "\n";

    for(int i = smallestUnacknowledgedFrame; i < smallestUnacknowledgedFrame + windowSize+2 && i < totalFrames; i++)
        if(receiveWindow[i] == 0){
            smallestUnacknowledgedFrame = i;
            break;
        }
    
    if(receiveWindow[smallestUnacknowledgedFrame] == 1)
        return;
    // smallestUnacknowledgedFrame = i + z;
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